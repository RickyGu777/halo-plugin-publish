package top.leftblue.publish.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import top.leftblue.publish.constant.MWAConst;
import top.leftblue.publish.dto.MWACmd;
import top.leftblue.publish.dto.MWAPost;
import top.leftblue.publish.module.NewPost;
import top.leftblue.publish.service.MetaWebLogService;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map<String, Object>> getUsersBlogs(String appKey, String username, String password) {
        return null;
    }

    @Override
    public String newPost(String blogid, String username, String password, MWAPost post, boolean publish) {
        return null;
    }

    @Override
    public boolean editPost(String postid, String username, String password, Map<String, Object> post, boolean publish) {
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

    private void newPost(MWACmd mwaCmd) {
        // NewPost newPost = NewPost.INSTANCE.from(mwaCmd);
    }


}
