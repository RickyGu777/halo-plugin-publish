package top.leftblue.publish.metaweblog.module;

import lombok.Data;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.metaweblog.annotation.MWAElementStruct;
import top.leftblue.publish.metaweblog.annotation.MWAElementWrapper;
import top.leftblue.publish.metaweblog.annotation.MWARootElement;
import top.leftblue.publish.service.impl.PublishServiceImpl;

@Data
@MWARootElement("methodCall")
public class EditPostMethodCall {

    private String methodName;

    @MWAElementWrapper("params")
    private MethodContent methodContent;

    @Data
    public static class MethodContent {

        private String postid;
        private String username;
        private String password;
        @MWAElementStruct
        private MethodPost methodPost;
        private Boolean publish;

    }

    public static EditPostMethodCall build(Post post, PublishServiceImpl.HeadContent content, MetaWeblogConfig config, String postid){
        MethodPost methodPost = new MethodPost();
        methodPost.setTitle(post.getSpec().getTitle());
        methodPost.setDescription(content.getRaw());

        MethodContent methodContent = new MethodContent();
        methodContent.setPostid(postid);
        methodContent.setUsername(config.getUsername());
        methodContent.setPassword(config.getPassword());
        methodContent.setMethodPost(methodPost);
        methodContent.setPublish(true);

        EditPostMethodCall newPostMethodCall = new EditPostMethodCall();
        newPostMethodCall.setMethodContent(methodContent);

        return newPostMethodCall;
    }
}
