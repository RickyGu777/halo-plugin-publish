package top.leftblue.publish.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.halo.LoginRequest;
import top.leftblue.publish.halo.PostRequest;
import top.leftblue.publish.halo.PublicKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class HaloApi {

    interface ApiPath {

        String newPost = "/apis/api.console.halo.run/v1alpha1/posts";

        String publish = "/apis/api.console.halo.run/v1alpha1/posts/{}/publish";

        String updateContent = "/apis/api.console.halo.run/v1alpha1/posts/{}/content";

        String publicKey = "/login/public-key";

        String login = "/login";

    }

    private final Config config;

    public Optional<Post> updateContent(PostRequest.Content content, String postid, String cookie) {
        return config.getBasicConfig()
                .flatMap(basicConfig -> {
                    String updateContentUrl = basicConfig.getHalourl() + StrUtil.format(ApiPath.updateContent, postid);
                    HttpRequest request = HttpUtil.createRequest(Method.PUT, updateContentUrl);
                    request.cookie(cookie);
                    request.body(JSONObject.toJSONString(content));
                    HttpResponse response = request.execute();
                    Optional<Post> post = Optional.of(JSONObject.parseObject(response.body(), Post.class));
                    response.close();
                    return post;
                });
    }

    public Optional<Post> postDraft(PostRequest postRequest, String cookie) {
        return config.getBasicConfig()
                .flatMap(basicConfig -> {
                    String newPostUrl = basicConfig.getHalourl() + ApiPath.newPost;
                    HttpRequest request = HttpUtil.createRequest(Method.POST, newPostUrl);
                    request.cookie(cookie);
                    request.body(JSONObject.toJSONString(postRequest));
                    HttpResponse response = request.execute();
                    Optional<Post> postMono = Optional.of(JSONObject.parseObject(response.body(), Post.class));
                    response.close();
                    return postMono;
                });
    }

    public Optional<Boolean> publish(Post post, String cookie) {
        return config.getBasicConfig()
                .flatMap(basicConfig -> {
                    String publishUrl = StrUtil.format(basicConfig.getHalourl() + ApiPath.publish, post.getMetadata().getName());
                    HttpRequest request = HttpUtil.createRequest(Method.PUT, publishUrl);
                    request.setReadTimeout(3000);
                    request.cookie(cookie);
                    HttpResponse response = request.execute();
                    response.close();
                    return Optional.of(Boolean.TRUE);
                });
    }

    public Optional<PublicKey> getPublicKey() {
        return config.getBasicConfig().flatMap(basicConfig -> {
            String publicKeyUrl = basicConfig.getHalourl() + ApiPath.publicKey;
            HttpRequest request = HttpUtil.createRequest(Method.GET, publicKeyUrl);
            request.setReadTimeout(3000);
            HttpResponse response = request.execute();
            String body = response.body();
            response.close();
            return Optional.of(JSONObject.parseObject(body, PublicKey.class));
        });
    }

    public Optional<String> getCookie(LoginRequest loginRequest) {
        return config.getBasicConfig()
                .flatMap(basicConfig -> {
                    String loginUrl = basicConfig.getHalourl() + ApiPath.login;
                    HttpResponse response;
                    HttpRequest request = HttpUtil.createRequest(Method.POST, loginUrl);
                    Map<String, String> form = new HashMap<>();
                    form.put("_csrf", loginRequest.get_csrf());
                    form.put("username", loginRequest.getUsername());
                    form.put("password", loginRequest.getPassword());
                    request.formStr(form);
                    request.header("Content-Type", "application/x-www-form-urlencoded");
                    response = request.execute();
                    Optional<String> session = Optional.of(response.getCookie("SESSION").toString());
                    response.close();
                    return session;
                });
    }


}
