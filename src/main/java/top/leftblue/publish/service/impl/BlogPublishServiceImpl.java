package top.leftblue.publish.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.ExtensionClient;
import top.leftblue.publish.config.BasicConfig;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.service.BlogPublishService;
import top.leftblue.publish.util.MapperUtil;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BlogPublishServiceImpl implements BlogPublishService {

    private final ExtensionClient client;
    private final Config config;
    private final CnBlogPublisherImpl cnBlogPublisher;

    @Override
    public void publish(String postName, String cookie) {
        BasicConfig basicConfig = config.getBasicConfig();
        List<String> platforms = basicConfig.getPlatforms();
        if (platforms.contains("cnblog")) {
            Post post = client.fetch(Post.class, postName).orElse(null);
            HeadContent content;
            try {
                content = getContent(basicConfig.getHalourl(), postName, cookie);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            cnBlogPublisher.publish(post, content);
        }
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
