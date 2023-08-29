package top.leftblue.publish.metaweblog.client;

import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.impl.PublishServiceImpl;

public interface MetaWeblogClient {

    MethodResponse newPost(Post post, PublishServiceImpl.HeadContent content, MetaWeblogConfig config);

    MethodResponse editPost(Post post, PublishServiceImpl.HeadContent content, MetaWeblogConfig config, String postid);

    MethodResponse callMethod(Object methodCall, String hostUrl);
}
