package top.leftblue.publish.metaweblog.server;

import reactor.core.publisher.Mono;
import top.leftblue.publish.metaweblog.module.NewPostMethodCall;

import java.util.List;
import java.util.Map;

public interface MetaWebLogServer {

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
     *
     * @param postid
     * @param username
     * @param password
     * @param post
     * @param publish
     * @return
     */
    boolean editPost(String postid, String username, String password, Map<String, Object> post, boolean publish);

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
}
