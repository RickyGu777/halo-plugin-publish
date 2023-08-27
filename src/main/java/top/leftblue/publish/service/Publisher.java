package top.leftblue.publish.service;

import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.dto.MWAResult;
import top.leftblue.publish.service.impl.BlogPublishServiceImpl;

public interface Publisher {

    MWAResult publish(Post post, BlogPublishServiceImpl.HeadContent content);

}
