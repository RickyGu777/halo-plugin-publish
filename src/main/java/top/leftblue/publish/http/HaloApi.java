package top.leftblue.publish.http;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.plugin.ReactiveSettingFetcher;
import top.leftblue.publish.config.BasicConfig;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.halo.LoginRequest;
import top.leftblue.publish.halo.PostRequest;
import top.leftblue.publish.halo.PublicKey;
import top.leftblue.publish.util.MapperUtil;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class HaloApi {

    private final ReactiveSettingFetcher settingFetcher;
    private final Config config;

    public Mono<Post> postDraft(PostRequest postRequest, String cookie) {
        return settingFetcher.fetch("basic", BasicConfig.class)
                .flatMap(basicConfig -> {
                    String newPostUrl = basicConfig.getHalourl() + HaloApiPath.newPost;
                    try {
                        HttpRequest request = HttpUtil.createRequest(Method.POST, newPostUrl);
                        request.cookie(cookie);
                        request.body(MapperUtil.bean2JsonStr(postRequest));
                        HttpResponse response = request.execute();
                        Mono<Post> postMono = Mono.just(MapperUtil.jsonStr2Bean(response.body(), Post.class));
                        response.close();
                        return postMono;
                    } catch (JsonProcessingException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }

    public Mono<Boolean> publish(Post post, String cookie) {
        return settingFetcher.fetch("basic", BasicConfig.class)
                .flatMap(basicConfig -> {
                    String publishUrl = StrUtil.format(basicConfig.getHalourl() + HaloApiPath.publish, post.getMetadata().getName());
                    try {
                        HttpRequest request = HttpUtil.createRequest(Method.PUT, publishUrl);
                        request.cookie(cookie);
                        HttpResponse response = request.execute();
                        response.close();
                        return Mono.just(Boolean.TRUE);
                    } catch (Exception e) {
                        return Mono.just(Boolean.FALSE);
                    }
                });
    }

    public Optional<PublicKey> getPublicKey() {
        return config.getBasicConfig2().flatMap(basicConfig -> {
            log.debug("getPublicKey basicConfig");
            String publicKeyUrl = basicConfig.getHalourl() + HaloApiPath.publicKey;
            log.debug("publicKeyUrl {}", publicKeyUrl);
            try {
                HttpRequest request = HttpUtil.createRequest(Method.GET, publicKeyUrl);
                request.setReadTimeout(3000);
                log.debug("getPublicKey basicConfig0");
                HttpResponse response = request.execute();
                log.debug("getPublicKey basicConfig1");
                String body = response.body();
                log.debug("getPublicKey basicConfig2");
                response.close();
                log.debug("getPublicKey basicConfig3 {}", body);
                return Optional.of(MapperUtil.jsonStr2Bean(body, PublicKey.class));
            } catch (JsonProcessingException e) {
                log.debug("getPublicKey basicConfig3");
                throw new RuntimeException(e);
            }
        });
    }

    public Optional<String> getCookie(LoginRequest loginRequest) {
        return config.getBasicConfig2()
                .flatMap(basicConfig -> {
                    System.out.println("11111111111111111111111111");
                    String loginUrl = basicConfig.getHalourl() + HaloApiPath.login;
                    HttpResponse response;
                    try {
                        HttpRequest request = HttpUtil.createRequest(Method.POST, loginUrl);
                        request.body(MapperUtil.bean2JsonStr(loginRequest));
                        log.debug("loginRequest {}", loginRequest);
                        response = request.execute();
                        log.debug("SESSION {}", response.getCookies());
                        Optional<String> session = Optional.of(response.getCookie("SESSION").getValue());
                        response.close();
                        return session;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


}
