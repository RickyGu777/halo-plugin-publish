package top.leftblue.publish.metaweblog.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import top.leftblue.publish.metaweblog.MWAConst;
import top.leftblue.publish.metaweblog.module.EditPostMethodCall;
import top.leftblue.publish.metaweblog.module.NewPostMethodCall;
import top.leftblue.publish.util.MapperUtil;

import java.util.List;
import java.util.Map;

public interface MetaWebLogServer {

    Logger log = LoggerFactory.getLogger(MetaWebLogServer.class);

    Mono<String> handleMethodCall(String xml);

    /**
     * 获取博客信息：blogger.getUsersBlogs
     *
     * @param appKey
     * @param username
     * @param password
     * @return
     */
    List<Map<String, Object>> getUsersBlogs(String appKey, String username, String password);

    /**
     * 发布博客文章：metaWeblog.newPost
     */
    String newPost(NewPostMethodCall methodCall);

    /**
     * 编辑博客文章：metaWeblog.editPost
     */
    String editPost(EditPostMethodCall methodCall);

    /**
     * 获取博客文章：metaWeblog.getPost
     *
     * @param postid
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> getPost(String postid, String username, String password);

    /**
     * 获取博客分类：metaWeblog.getCategories
     *
     * @param blogid
     * @param username
     * @param password
     * @return
     */
    List<Map<String, String>> getCategories(String blogid, String username, String password);

    /**
     * 获取最近的文章列表：metaWeblog.getRecentPosts
     *
     * @param blogid
     * @param username
     * @param password
     * @param numberOfPosts
     * @return
     */
    List<Map<String, Object>> getRecentPosts(String blogid, String username, String password, int numberOfPosts);

    /**
     * 上传媒体对象：metaWeblog.newMediaObject
     *
     * @param blogid
     * @param username
     * @param password
     * @param post
     * @return
     */
    Map<String, String> newMediaObject(String blogid, String username, String password, Map<String, Object> post);

    default String toResultText(Object obj) {
        try {
            return MapperUtil.bean2Xml(obj);
        } catch (Exception e) {
            log.error("halo-plugin-publish: failed cast MethodResponse to string, {}", e.getMessage());
            return MWAConst.INTERNAL_ERROR;
        }
    }
}
