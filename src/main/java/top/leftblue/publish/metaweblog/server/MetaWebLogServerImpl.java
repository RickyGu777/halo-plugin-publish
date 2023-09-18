package top.leftblue.publish.metaweblog.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import run.halo.app.extension.ExtensionClient;
import run.halo.app.extension.Metadata;
import top.leftblue.publish.halo.LoginRequest;
import top.leftblue.publish.halo.PostRequest;
import top.leftblue.publish.http.HaloApi;
import top.leftblue.publish.metaweblog.MWAConst;
import top.leftblue.publish.metaweblog.exception.NoCookieException;
import top.leftblue.publish.metaweblog.exception.PostNotFoundException;
import top.leftblue.publish.metaweblog.module.*;
import top.leftblue.publish.util.MapperUtil;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static javax.crypto.Cipher.ENCRYPT_MODE;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetaWebLogServerImpl implements MetaWebLogServer {

    private final ExtensionClient client;
    private final HaloApi haloApi;

    @Override
    public Mono<String> handleMethodCall(String xml) {
        AtomicReference<String> result = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                MethodCall methodCall = MapperUtil.xml2Bean(xml, MethodCall.class);
                log.debug("halo-plugin-publish: receive method '{}'", methodCall.getMethodName());
                String resultXml = switch (methodCall.getMethodName()) {
                    case MWAConst.newPost -> newPost(MapperUtil.xml2Bean(xml, NewPostMethodCall.class));
                    case MWAConst.editPost -> editPost(MapperUtil.xml2Bean(xml, EditPostMethodCall.class));
                    default -> MWAConst.METHOD_NOT_SUPPORT;
                };
                result.set(resultXml);
            } catch (Exception e) {
                log.error("halo-plugin-publish: internal error", e);
                result.set(MWAConst.INTERNAL_ERROR);
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(result.get());
    }

    @Override
    public List<Map<String, Object>> getUsersBlogs(String appKey, String username, String password) {
        return null;
    }

    @Override
    public String newPost(NewPostMethodCall methodCall) {
        log.debug("halo-plugin-publish: receive newPost method call");
        Post post = buildPost(methodCall);
        PostRequest.Content content = buildContent(methodCall.getMethodContent().getMethodPost().getDescription());
        PostRequest postRequest = new PostRequest(post, content);
        getCookie(methodCall.getMethodContent().getUsername(), methodCall.getMethodContent().getPassword())
                .or(() -> {
                    throw new NoCookieException();
                }).ifPresent(cookie -> {
                    log.debug("halo-plugin-publish: new post");
                    haloApi.postDraft(postRequest, cookie)
                            .or(() -> {
                                throw new RuntimeException("New post failed");
                            }).ifPresent(postResponse -> {
                                if (methodCall.getMethodContent().getPublish()) {
                                    log.debug("halo-plugin-publish: publish post {}", postResponse.getMetadata().getName());
                                    haloApi.publish(postResponse, cookie);
                                }
                            });
                });
        MethodResponse response = MethodResponse.builder().result(MethodResponse.MethodResult.builder().postid(post.getMetadata().getName()).build()).build();
        return toResultText(response);
    }


    @Override
    public String editPost(EditPostMethodCall methodCall) {
        log.debug("halo-plugin-publish: receive editPost method call");
        MethodEditResponse editResponse = MethodEditResponse.builder().build();
        try {
            client.fetch(Post.class, methodCall.getMethodContent().getPostid()).or(() -> {
                log.debug("halo-plugin-publish: post witch need edit is not exist {}", methodCall.getMethodContent().getPostid());
                throw new PostNotFoundException("您要编辑的文章不存在");
            }).flatMap(post -> getCookie(methodCall.getMethodContent().getUsername(), methodCall.getMethodContent().getPassword()).or(() -> {
                throw new NoCookieException();
            })).ifPresent(cookie -> {
                log.debug("halo-plugin-publish: edit post {}", methodCall.getMethodContent().getPostid());
                PostRequest.Content content = buildContent(methodCall.getMethodContent().getMethodPost().getDescription());
                haloApi.updateContent(content, methodCall.getMethodContent().getPostid(), cookie).ifPresent(postResponse -> {
                    if (methodCall.getMethodContent().getPublish()) {
                        log.debug("halo-plugin-publish: publish post {}", methodCall.getMethodContent().getPostid());
                        haloApi.publish(postResponse, cookie);
                    }
                    editResponse.setResult(MethodEditResponse.MethodResult.builder().success(true).build());
                });
            });
        } catch (Exception e) {
            editResponse.setFault(MethodEditResponse.Fault.builder().faultCode(1).faultString(e.getMessage()).build());
        }
        return toResultText(editResponse);
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
        return haloApi.getPublicKey().flatMap(publicKeyStr -> {
            try {
                byte[] originKey = Base64.getDecoder().decode(publicKeyStr.getBase64Format());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(originKey));
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(ENCRYPT_MODE, publicKey);
                byte[] encryptedPassword = cipher.doFinal(password.getBytes());
                String base64Password = Base64.getEncoder().encodeToString(encryptedPassword);
                return haloApi.getCookie(LoginRequest.builder()._csrf(UUID.randomUUID().toString()).username(username).password(base64Password).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Post buildPost(NewPostMethodCall methodCall) {
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

    private PostRequest.Content buildContent(String text) {
        boolean isHtml = MapperUtil.isXml(text);
        if (isHtml) {
            return new PostRequest.Content(text, text, "HTML");
        } else {
            String html = MapperUtil.markdown2Html(text);
            return new PostRequest.Content(text, html, "MARKDOWN");
        }
    }
}
