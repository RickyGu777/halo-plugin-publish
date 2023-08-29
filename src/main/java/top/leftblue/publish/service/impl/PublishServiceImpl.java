package top.leftblue.publish.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.Metadata;
import top.leftblue.publish.config.BasicConfig;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.constant.Platform;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.module.PublishPost;
import top.leftblue.publish.service.PublishService;
import top.leftblue.publish.service.Publisher;
import top.leftblue.publish.util.BeanUtil;
import top.leftblue.publish.util.MapperUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class PublishServiceImpl implements PublishService {

    private final ExtensionClient client;
    private final Config config;

    @Override
    public void publish(String postName, String cookie) {
        BasicConfig basicConfig = config.getBasicConfig();
        List<String> platforms = basicConfig.getPlatforms();

        Post post = client.fetch(Post.class, postName).orElse(null);
        HeadContent content;
        try {
            content = getContent(basicConfig.getHalourl(), postName, cookie);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        PublishPost publishPost = client.fetch(PublishPost.class, postName).orElse(null);
        log.debug("publishPost: " + publishPost);
        for (String platformName : platforms) {
            Platform platform = Platform.from(platformName);
            if (platform != null) {
                Class<? extends Publisher> handleClz = platform.getHandleClz();
                Publisher publisher = BeanUtil.getBean(handleClz);
                PublishPost.SitePost sitePost = null;
                if (publishPost != null) {
                    sitePost = publishPost.getSitePosts().stream().filter(s -> platform.getName().equals(s.getSite())).findAny().orElse(null);
                }
                MethodResponse response;
                if (sitePost == null) {
                    response = publisher.publish(post, content);
                    addPublishPost(postName, platformName, response.getResult().getPostid());
                } else {
                    response = publisher.edit(post, content, sitePost.getPostid());
                }
            }
        }
    }

    private void addPublishPost(String postName, String platformName, String postid) {
        PublishPost publishPost = new PublishPost();
        Metadata metadata = new Metadata();
        metadata.setName(postName);
        publishPost.setMetadata(metadata);
        publishPost.setName(postName);
        publishPost.setSitePosts(new ArrayList<>());
        PublishPost.SitePost sitePost = new PublishPost.SitePost();
        sitePost.setSite(platformName);
        sitePost.setPostid(postid);
        publishPost.getSitePosts().add(sitePost);
        client.create(publishPost);
    }

    private HeadContent getContent(String halourl, String postName, String cookie) throws JsonProcessingException {
        if (halourl.charAt(halourl.length() - 1) == '/') {
            halourl = halourl.substring(0, halourl.length() - 1);
        }
        String path = StrUtil.format("{}/apis/api.console.halo.run/v1alpha1/posts/{}/head-content", halourl, postName);
        HttpRequest get = HttpUtil.createGet(path);
        get.header("Cookie", cookie);
        String body = get.execute().sync().body();
        return MapperUtil.jsonStr2Bean(body, HeadContent.class);
    }

    @Data
    public static class HeadContent {
        private String content;
        private String raw;
        private String rawType;
        private String snapshotName;
    }

}
