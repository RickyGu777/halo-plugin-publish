package top.leftblue.publish.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.leftblue.publish.constant.MWAConst;
import top.leftblue.publish.dto.MWACmd;
import top.leftblue.publish.module.NewPost;
import top.leftblue.publish.service.MetaWebLogService;

@RequiredArgsConstructor
@Component
public class MetaWebLogServiceImpl implements MetaWebLogService {

    private static final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public Mono<MWACmd> convertContent(String bodyXml) {
        try {
            return Mono.just(xmlMapper.readValue(bodyXml, MWACmd.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<String> publish(MWACmd mwaCmd) {
        switch (mwaCmd.getMethodName()) {
            case MWAConst.Method.newPost -> newPost(mwaCmd);
        }
        return Mono.just("测试");
    }

    private void newPost(MWACmd mwaCmd) {
        NewPost newPost = NewPost.INSTANCE.from(mwaCmd);
        

    }

}
