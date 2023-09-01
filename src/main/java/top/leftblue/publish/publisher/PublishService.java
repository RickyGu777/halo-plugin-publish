package top.leftblue.publish.publisher;

import run.halo.app.core.extension.content.Post;

public interface PublishService {

    void publish(Post post);

    void publish(String postName);

}
