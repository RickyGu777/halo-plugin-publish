package top.leftblue.publish.publisher;

import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;

import java.util.Optional;

public interface Publisher {

    Optional<MethodResponse> publish(Post post, ContentWrapper content);

    Optional<MethodResponse> edit(Post post, ContentWrapper content, String postId);

}
