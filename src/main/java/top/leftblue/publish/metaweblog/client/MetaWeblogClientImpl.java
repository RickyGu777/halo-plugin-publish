package top.leftblue.publish.metaweblog.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.config.MetaWeblogConfig;
import top.leftblue.publish.http.MeatWeblogApi;
import top.leftblue.publish.metaweblog.module.EditPostMethodCall;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.metaweblog.module.NewPostMethodCall;
import top.leftblue.publish.service.impl.PublishServiceImpl;
import top.leftblue.publish.util.MapperUtil;

@Slf4j
@RequiredArgsConstructor
@Component
public class MetaWeblogClientImpl implements MetaWeblogClient {

    private final MeatWeblogApi meatWeblogApi;

    @Override
    public MethodResponse newPost(Post post, PublishServiceImpl.HeadContent content, MetaWeblogConfig config) {
        NewPostMethodCall methodCall = NewPostMethodCall.build(post, content, config);
        methodCall.setMethodName("metaWeblog.newPost");
        return callMethod(methodCall, config.getBaseurl());
    }


    @Override
    public MethodResponse editPost(Post post, PublishServiceImpl.HeadContent content, MetaWeblogConfig config, String postid) {
        EditPostMethodCall methodCall = EditPostMethodCall.build(post, content, config, postid);
        log.debug("methodCall:" + methodCall);
        methodCall.setMethodName("metaWeblog.editPost");
        return callMethod(methodCall, config.getBaseurl());
    }


    @Override
    public MethodResponse callMethod(Object methodCall, String hostUrl) {
        try {
            String xmlContent = MapperUtil.bean2Xml(methodCall);
            log.debug("methodCall: " + xmlContent);
            String xmlBody = meatWeblogApi.doMethod(hostUrl, xmlContent);
            log.debug("methodResponse: " + xmlContent);
            return MapperUtil.xml2Bean(xmlBody, MethodResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
