package top.leftblue.publish.publisher.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.constant.PostSite;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.client.MetaWeblogClient;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.publisher.Publisher;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CnBlogPublisher implements Publisher {

    private final Config config;
    private final MetaWeblogClient metaWeblogClient;

    @Override
    public PostSite getSite() {
        return PostSite.CNBLOG;
    }

    @Override
    public Optional<MethodResponse> publish(Post post, ContentWrapper content) {
        return config.getMetaWeblogConfig(getSite().getName())
                .map(config -> metaWeblogClient.newPost(post, content, config));

    }

    @Override
    public Optional<MethodResponse> edit(Post post, ContentWrapper content, String postId) {
        return config.getMetaWeblogConfig(getSite().getName())
                .map(config -> metaWeblogClient.editPost(post, content, config, postId));
    }

}
