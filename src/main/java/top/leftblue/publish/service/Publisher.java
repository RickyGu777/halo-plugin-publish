package top.leftblue.publish.service;

import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.impl.PublishServiceImpl;

public interface Publisher {

    MethodResponse publish(Post post, PublishServiceImpl.HeadContent content);

    MethodResponse edit(Post post, PublishServiceImpl.HeadContent content, String postId);

}
