package top.leftblue.publish.publisher.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Snapshot;
import run.halo.app.extension.ExtensionClient;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.constant.Platform;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.module.PublishPost;
import top.leftblue.publish.publisher.PublishService;
import top.leftblue.publish.publisher.Publisher;
import top.leftblue.publish.util.BeanUtil;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PublishServiceImpl implements PublishService {

    private final ExtensionClient client;
    private final Config config;

    @Override
    public void publish(Post post) {
        client.fetch(Snapshot.class, post.getSpec().getBaseSnapshot()).flatMap(baseSnapshot -> {
            if (StringUtils.equals(post.getSpec().getHeadSnapshot(), post.getSpec().getBaseSnapshot())) {
                var contentWrapper = ContentWrapper.patchSnapshot(baseSnapshot, baseSnapshot);
                return Optional.of(contentWrapper);
            }
            return client.fetch(Snapshot.class, post.getSpec().getHeadSnapshot())
                    .map(snapshot -> ContentWrapper.patchSnapshot(snapshot, baseSnapshot));
        }).ifPresent(content -> {
            this.publish(post, content);
        });
    }

    @Override
    public void publish(String postName) {
        client.fetch(Post.class, postName).ifPresent(this::publish);
    }

    private void publish(Post post, ContentWrapper content) {
        System.out.println("snapshot: " + content);
        config.getBasicConfig().flatMap(basicConfig ->
                client.fetch(PublishPost.class, post.getMetadata().getName())
                        .or(() -> {
                            client.create(PublishPost.newBuild(post.getMetadata().getName()));
                            return client.fetch(PublishPost.class, post.getMetadata().getName());
                        }).flatMap(publishPost -> {
                                    MethodResponse response = publish(post, content, basicConfig.getPlatforms(), publishPost);
                                    return Optional.of(response);
                                }
                        ));
    }

    private MethodResponse publish(Post post, ContentWrapper content, List<String> platforms, PublishPost publishPost) {
        for (String platform : platforms) {
            Publisher publisher = BeanUtil.getBean(Platform.from(platform).getHandleClz());
            PublishPost.SitePost sitePost = publishPost.getSitePosts().stream().filter(s -> platform.equals(s.getSite())).findAny().orElse(null);
            if (sitePost == null) {
                publisher.publish(post, content)
                        .flatMap(response -> {
                            log.debug("新增文章");
                            if (response.getResult() != null) {
                                log.debug("新增成功");
                                addSitePost(platform, response.getResult().getPostid(), publishPost);
                            } else {
                                log.debug("新增失败");
                            }
                            return Optional.empty();
                        });
            } else {
                publisher.edit(post, content, sitePost.getPostid())
                        .flatMap(response -> {
                            log.debug("更新文章");
                            if (response.getFault() != null && "1".equals(response.getFault().getFaultCode())) {
                                log.debug("更新失败，文章不存在，尝试新增文章");
                                return publisher.publish(post, content);
                            }
                            return Optional.empty();
                        })
                        .flatMap(response -> {
                            log.debug("新增文章");
                            if (response.getResult() != null) {
                                log.debug("新增成功");
                                addSitePost(platform, response.getResult().getPostid(), publishPost);
                            }
                            return Optional.empty();
                        });

            }
        }
        return new MethodResponse();
    }

    private void addSitePost(String platformName, String postid, PublishPost publishPost) {
        publishPost.getSitePosts().removeIf(p -> p.getSite().equals(platformName));
        PublishPost.SitePost sitePost = new PublishPost.SitePost();
        sitePost.setSite(platformName);
        sitePost.setPostid(postid);
        publishPost.getSitePosts().add(sitePost);
        client.update(publishPost);
    }

    private HeadContent getContent(String halourl, String postName, String cookie) {
        if (halourl.charAt(halourl.length() - 1) == '/') {
            halourl = halourl.substring(0, halourl.length() - 1);
        }
        String path = StrUtil.format("{}/apis/api.console.halo.run/v1alpha1/posts/{}/head-content", halourl, postName);
        HttpRequest get = HttpUtil.createGet(path);
        get.header("Cookie", cookie);
        String body = get.execute().sync().body();
        return JSONObject.parseObject(body, HeadContent.class);
    }

    @Data
    public static class HeadContent {
        private String content;
        private String raw;
        private String rawType;
        private String snapshotName;
    }

}
