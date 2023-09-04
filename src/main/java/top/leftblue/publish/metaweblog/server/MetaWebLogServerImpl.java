package top.leftblue.publish.metaweblog.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.Metadata;
import run.halo.app.extension.ReactiveExtensionClient;
import top.leftblue.publish.halo.LoginRequest;
import top.leftblue.publish.halo.PostRequest;
import top.leftblue.publish.http.HaloApi;
import top.leftblue.publish.metaweblog.MWAConst;
import top.leftblue.publish.metaweblog.module.EditPostMethodCall;
import top.leftblue.publish.metaweblog.module.MethodCall;
import top.leftblue.publish.metaweblog.module.NewPostMethodCall;
import top.leftblue.publish.util.MapperUtil;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import static javax.crypto.Cipher.ENCRYPT_MODE;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetaWebLogServerImpl implements MetaWebLogServer {

    private final ReactiveExtensionClient client;
    private final HaloApi haloApi;

    @Override
    public Mono<String> handleMethodCall(String xml) {
        try {
            MethodCall methodCall = MapperUtil.xml2Bean(xml, MethodCall.class);
            log.debug("halo-plugin-publish: receive method '{}'", methodCall.getMethodName());
            switch (methodCall.getMethodName()) {
                case MWAConst.newPost -> newPost(MapperUtil.xml2Bean(xml, NewPostMethodCall.class));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Mono.just("""
                <?xml version="1.0" encoding="utf-8"?>
                <methodResponse>
                  <params>
                    <param>
                      <value>
                        <string>17662739</string>
                      </value>
                    </param>
                  </params>
                </methodResponse>""");
    }

    @Override
    public List<Map<String, Object>> getUsersBlogs(String appKey, String username, String password) {
        return null;
    }

    @Override
    public String newPost(NewPostMethodCall methodCall) {
        log.debug("halo-plugin-publish: convert methodCall '{}'", methodCall);
        Post post = methodCall2Post(methodCall);
        PostRequest.Content content = buildContent(methodCall.getMethodContent().getMethodPost().getDescription());
        PostRequest postRequest = new PostRequest(post, content);

        log.debug("halo-plugin-publish: start new post");
        getCookie(methodCall.getMethodContent().getUsername(), methodCall.getMethodContent().getPassword())
                .ifPresentOrElse(cookie -> {
                            log.debug("halo-plugin-publish: post draft with cookie '{}'", cookie);
                            haloApi.postDraft(postRequest, cookie)
                                    .ifPresent(postResponse -> {
                                        if (methodCall.getMethodContent().getPublish()) {
                                            log.debug("halo-plugin-publish: publish with cookie '{}'", cookie);
                                            haloApi.publish(postResponse, cookie);
                                        }
                                    });
                        },
                        () -> log.debug("halo-plugin-publish: can not publish with out cookie, no cookie in login result")
                );

        return null;
    }

    private PostRequest.Content buildContent(String text) {
        boolean isHtml = MapperUtil.isHtml(text);
        if (isHtml) {
            return new PostRequest.Content(text, text, "HTML");
        } else {
            String html = MapperUtil.markdown2Html(text);
            return new PostRequest.Content(text, html, "MARKDOWN");
        }
    }

    @Override
    public boolean editPost(EditPostMethodCall methodCall) {
        return false;
    }

    @Override
    public Map<String, Object> getPost(String postid, String username, String password) {
        return null;
    }

    @Override
    public List<Map<String, String>> getCategories(String blogid, String username, String password) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getRecentPosts(String blogid, String username, String password, int numberOfPosts) {
        return null;
    }

    @Override
    public Map<String, String> newMediaObject(String blogid, String username, String password, Map<String, Object> post) {
        return null;
    }

    private Optional<String> getCookie(String username, String password) {
        return haloApi.getPublicKey()
                .flatMap(publicKeyStr -> {
                    try {
                        log.debug("password {}", password);
                        log.debug("getCookie publicKeyStr");
                        byte[] originKey = Base64.getDecoder().decode(publicKeyStr.getBase64Format());
                        log.debug("originKey {}", originKey);
                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(originKey));
                        Cipher cipher = Cipher.getInstance("RSA");
                        cipher.init(ENCRYPT_MODE, publicKey);
                        byte[] encryptedPassword = cipher.doFinal(password.getBytes());
                        log.debug("encryptedPassword {}", encryptedPassword);
                        String base64Password = Base64.getEncoder().encodeToString(encryptedPassword);
                        log.debug("getCookie base64Password {}", base64Password);
                        return haloApi.getCookie(LoginRequest.builder()._csrf(UUID.randomUUID().toString()).username(username).password(base64Password).build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Post methodCall2Post(NewPostMethodCall methodCall) {
        Post post = new Post();
        post.setApiVersion("content.halo.run/v1alpha1");
        post.setKind("Post");

        Metadata metadata = new Metadata();
        post.setMetadata(metadata);
        metadata.setName(UUID.randomUUID().toString());
        metadata.setAnnotations(Map.of("content.halo.run/preferred-editor", "default"));

        Post.PostSpec spec = new Post.PostSpec();
        post.setSpec(spec);
        spec.setAllowComment(true);
        spec.setCategories(List.of());
        spec.setCover("");
        spec.setDeleted(false);
        Post.Excerpt excerpt = new Post.Excerpt();
        excerpt.setRaw("");
        excerpt.setAutoGenerate(true);
        spec.setExcerpt(excerpt);
        spec.setHtmlMetas(List.of());
        spec.setPinned(false);
        spec.setPriority(0);
        spec.setPublish(methodCall.getMethodContent().getPublish());
        spec.setSlug(String.valueOf(System.currentTimeMillis()));
        spec.setTags(List.of());
        spec.setTemplate("");
        spec.setTitle(methodCall.getMethodContent().getMethodPost().getTitle());
        spec.setVisible(Post.VisibleEnum.PUBLIC);
        return post;
    }
}
