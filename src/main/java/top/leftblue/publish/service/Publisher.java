package top.leftblue.publish.service;

import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.core.extension.content.Snapshot;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.impl.PublishServiceImpl;

public interface Publisher {

    Mono<MethodResponse> publish(Post post, ContentWrapper content);

    Mono<MethodResponse> edit(Post post, ContentWrapper content, String postId);

}
