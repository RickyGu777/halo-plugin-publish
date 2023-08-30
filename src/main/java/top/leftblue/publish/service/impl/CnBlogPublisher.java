package top.leftblue.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Snapshot;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.constant.Platform;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.client.MetaWeblogClient;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.Publisher;

@RequiredArgsConstructor
@Component
public class CnBlogPublisher implements Publisher {

    private final Config config;
    private final MetaWeblogClient metaWeblogClient;

    @Override
    public Mono<MethodResponse> publish(Post post, ContentWrapper content) {
        return config.getMetaWeblogConfig(Platform.CNBLOG.getName())
                .map(config -> metaWeblogClient.newPost(post, content, config));

    }

    @Override
    public Mono<MethodResponse> edit(Post post, ContentWrapper content, String postId) {
        return config.getMetaWeblogConfig(Platform.CNBLOG.getName())
         .map(config -> metaWeblogClient.editPost(post, content, config, postId));
    }

}
