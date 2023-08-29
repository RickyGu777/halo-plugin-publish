package top.leftblue.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.constant.Platform;
import top.leftblue.publish.metaweblog.client.MetaWeblogClient;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.Publisher;

@RequiredArgsConstructor
@Component
public class CnBlogPublisher implements Publisher {

    private final Config config;
    private final MetaWeblogClient metaWeblogClient;

    @Override
    public MethodResponse publish(Post post, PublishServiceImpl.HeadContent content) {
        MetaWeblogConfig metaWeblogConfig = config.getMetaWeblogConfig(Platform.CNBLOG.getName());
        return metaWeblogClient.newPost(post, content, metaWeblogConfig);
    }

    @Override
    public MethodResponse edit(Post post, PublishServiceImpl.HeadContent content, String postId) {
        MetaWeblogConfig metaWeblogConfig = config.getMetaWeblogConfig(Platform.CNBLOG.getName());
        return metaWeblogClient.editPost(post, content, metaWeblogConfig, postId);
    }

}
