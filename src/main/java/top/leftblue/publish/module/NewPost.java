package top.leftblue.publish.module;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.leftblue.publish.dto.MWACmd;
import top.leftblue.publish.dto.MWAPost;

import java.util.ArrayList;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewPost extends ModuleConvertor<NewPost> {

    public static final NewPost INSTANCE = new NewPost();

    private String blogid;
    private String username;
    private String password;
    private MWAPost mwaPost;
    private Boolean publish;

    @Override
    public NewPost from(MWACmd mwaCmd) {
        NewPost newPost = new NewPost();
        MWAPost mwaPost = new MWAPost();
        newPost.setMwaPost(mwaPost);

        String blogId = mwaCmd.getParams().get(0).getValue().getStrValue();
        String username = mwaCmd.getParams().get(1).getValue().getStrValue();
        String password = mwaCmd.getParams().get(2).getValue().getStrValue();
        Boolean publish = mwaCmd.getParams().get(4).getValue().getBoolValue();
        newPost.setBlogid(blogId);
        newPost.setUsername(username);
        newPost.setPassword(password);
        newPost.setPublish(publish);
        mwaCmd.getParams().get(3).getValue().getMembers().forEach(member -> {
            switch (member.getStrName()) {
                case MWAPost.Fields.title -> mwaPost.setTitle(member.getStrValue().getStrValue());
                case MWAPost.Fields.description -> mwaPost.setDescription(member.getStrValue().getStrValue());
            }
        });

        return newPost;
    }


    public MWACmd toMWARequest() {
        MWACmd mwaCmd = new MWACmd("metaWeblog.newPost", new ArrayList<>(5));
        MWACmd.Param postid =
                MWACmd.Param.builder().value(MWACmd.Value.builder().strValue("1").build()).build();
        MWACmd.Param username = MWACmd.Param.builder().value(MWACmd.Value.builder().strValue(this.username).build()).build();
        MWACmd.Param password = MWACmd.Param.builder().value(MWACmd.Value.builder().strValue(this.password).build()).build();
        MWACmd.Param members =
                MWACmd.Param.builder().value(MWACmd.Value.builder().members(new ArrayList<>()).build()).build();
        members.getValue().getMembers().add(MWACmd.Member.builder().strName(MWAPost.Fields.title).strValue(MWACmd.NameValue.builder().strValue(this.mwaPost.getTitle()).build()).build());
        members.getValue().getMembers().add(MWACmd.Member.builder().strName(MWAPost.Fields.description).strValue(MWACmd.NameValue.builder().strValue(this.mwaPost.getDescription()).build()).build());
        MWACmd.Param publish =
                MWACmd.Param.builder().value(MWACmd.Value.builder().strValue(String.valueOf(this.publish)).build()).build();
        mwaCmd.getParams().add(postid);
        mwaCmd.getParams().add(username);
        mwaCmd.getParams().add(password);
        mwaCmd.getParams().add(members);
        mwaCmd.getParams().add(publish);
        return mwaCmd;
    }
}
