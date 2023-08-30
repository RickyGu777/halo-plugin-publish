package top.leftblue.publish.metaweblog.client;

import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;

public interface MetaWeblogClient {

    MethodResponse newPost(Post post, ContentWrapper content, MetaWeblogConfig config);

    MethodResponse editPost(Post post, ContentWrapper content, MetaWeblogConfig config, String postid);

    MethodResponse callMethod(Object methodCall, String hostUrl);
}
