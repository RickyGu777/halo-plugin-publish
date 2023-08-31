package top.leftblue.publish.http;

public interface HaloApiPath {

    String newPost = "/apis/api.console.halo.run/v1alpha1/posts";

    String publish = "/apis/api.console.halo.run/v1alpha1/posts/{}/publish";

    String publicKey = "/login/public-key";

    String login = "/login";

}
