package top.leftblue.publish.publisher.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Snapshot;
import run.halo.app.extension.ExtensionClient;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.constant.PostSite;
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
        wrapContent(post).ifPresent(content -> {
            this.publish(post, content);
        });
    }

    @Override
    public void publish(String postName) {
        client.fetch(Post.class, postName).ifPresent(this::publish);
    }

    private void publish(Post post, ContentWrapper content) {
        config.getBasicConfig().ifPresent(basicConfig ->
                client.fetch(PublishPost.class, post.getMetadata().getName()).or(() -> {
                    client.create(PublishPost.newBuild(post.getMetadata().getName()));
                    return client.fetch(PublishPost.class, post.getMetadata().getName());
                }).ifPresent(publishPost -> {
                    publish(post, content, basicConfig.getPlatforms(), publishPost);
                }));
    }

    private void publish(Post post, ContentWrapper content, List<String> platforms, PublishPost publishPost) {
        for (String siteName : platforms) {
            Publisher publisher = BeanUtil.getBean(PostSite.from(siteName).getHandleClz());
            PublishPost.SitePost sitePost = publishPost.getSitePosts().stream().filter(s -> siteName.equals(s.getSite())).findAny().orElse(null);
            if (sitePost == null) {
                addPost(publisher, post, content, publishPost);
            } else {
                editPost(publisher, post, content, sitePost.getPostid(), publishPost);
            }
        }
    }

    private Optional<MethodResponse> addPost(Publisher publisher, Post post, ContentWrapper content, PublishPost publishPost) {
        log.debug("halo-plugin-publish: add post '{}'", post.getSpec().getTitle());
        return publisher.publish(post, content).flatMap(response -> {
            if (response.isFailed()) {
                log.debug("halo-plugin-publish: add post failed, code: {}, msg: {}", response.getFault().getFaultCode(), response.getFault().getFaultString());
            } else {
                log.debug("halo-plugin-publish: add post succeed");
                addSitePost(publisher.getSite().getName(), response.getResult().getPostid(), publishPost);
            }
            return Optional.of(response);
        });
    }

    private Optional<MethodResponse> editPost(Publisher publisher, Post post, ContentWrapper content, String postid, PublishPost publishPost) {
        log.debug("halo-plugin-publish: edit post '{}'", post.getSpec().getTitle());
        return publisher.edit(post, content, postid).flatMap(response -> {
            if (response.isFailed()) {
                log.debug("halo-plugin-publish: edit post failed, postid: {}, code: {}, msg: {}", postid, response.getFault().getFaultCode(), response.getFault().getFaultString());
                if ("1".equals(response.getFault().getFaultCode())) {
                    log.debug("halo-plugin-publish: post not exist, try add");
                    return addPost(publisher, post, content, publishPost);
                }
            } else {
                log.debug("halo-plugin-publish: edit post succeed");
            }
            return Optional.of(response);
        });
    }

    private void addSitePost(String siteName, String postid, PublishPost publishPost) {
        publishPost.getSitePosts().removeIf(p -> p.getSite().equals(siteName));
        PublishPost.SitePost sitePost = new PublishPost.SitePost();
        sitePost.setSite(siteName);
        sitePost.setPostid(postid);
        publishPost.getSitePosts().add(sitePost);
        client.update(publishPost);
    }

    private Optional<ContentWrapper> wrapContent(Post post) {
        return client.fetch(Snapshot.class, post.getSpec().getBaseSnapshot()).flatMap(baseSnapshot -> {
            if (StringUtils.equals(post.getSpec().getHeadSnapshot(), post.getSpec().getBaseSnapshot())) {
                var contentWrapper = ContentWrapper.patchSnapshot(baseSnapshot, baseSnapshot);
                return Optional.of(contentWrapper);
            }
            return client.fetch(Snapshot.class, post.getSpec().getHeadSnapshot()).map(snapshot -> ContentWrapper.patchSnapshot(snapshot, baseSnapshot));
        });
    }
}
