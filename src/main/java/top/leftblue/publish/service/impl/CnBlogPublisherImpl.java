package top.leftblue.publish.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.Config;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.constant.Platform;
import top.leftblue.publish.dto.MWACmd;
import top.leftblue.publish.dto.MWAResult;
import top.leftblue.publish.http.MeatWeblogApi;
import top.leftblue.publish.module.MWAPost;
import top.leftblue.publish.module.NewPost;
import top.leftblue.publish.service.Publisher;
import top.leftblue.publish.util.MapperUtil;

@RequiredArgsConstructor
@Component
public class CnBlogPublisherImpl implements Publisher {

    private final MeatWeblogApi meatWeblogApi;
    private final Config config;

    @Override
    public MWAResult publish(Post post, BlogPublishServiceImpl.HeadContent content) {
        MetaWeblogConfig metaWeblogConfig = config.getMetaWeblogConfig(Platform.CNBLOG.getName());
        MWAPost mwaPost = new MWAPost();
        mwaPost.setTitle(post.getSpec().getTitle());
        mwaPost.setDescription(content.getRaw());
        NewPost newPost = new NewPost();
        newPost.setUsername(metaWeblogConfig.getUsername());
        newPost.setPassword(metaWeblogConfig.getPassword());
        newPost.setMwaPost(mwaPost);
        newPost.setPublish(true);
        System.out.println(44444444);

        MWACmd mwaCmd = newPost.toMWARequest();
        MWAResult MWAResult;
        try {
            String xmlContent = MapperUtil.bean2XmlStr(mwaCmd);
            System.out.println("222222222222222222222 " + xmlContent);
            String xmlBody = meatWeblogApi.doMethod(metaWeblogConfig.getBaseurl(), xmlContent);
            System.out.println("555555555555555 " + xmlBody);
            MWAResult = MapperUtil.xmlStr2Bean(xmlBody, MWAResult.class);
            System.out.println(MWAResult);
            if (MWAResult.getFault() != null){
                mwaCmd.setMethodName("metaWeblog.editPost");
                xmlContent = MapperUtil.bean2XmlStr(mwaCmd);
                String s = meatWeblogApi.doMethod(metaWeblogConfig.getBaseurl(), xmlContent);
                System.out.println(s);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return MWAResult;
    }

}
