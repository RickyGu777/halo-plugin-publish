package top.leftblue.publish.publisher.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.constant.PostSite;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.publisher.Publisher;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class NonePublisher implements Publisher {

    @Override
    public PostSite getSite() {
        return PostSite.NONE;
    }

    @Override
    public Optional<MethodResponse> publish(Post post, ContentWrapper content) {
        return Optional.of(MethodResponse.builder().fault(MethodResponse.Fault.builder().faultCode("500").faultString("找不到对应网站的处理器").build()).build());
    }

    @Override
    public Optional<MethodResponse> edit(Post post, ContentWrapper content, String postId) {
        return Optional.of(MethodResponse.builder().fault(MethodResponse.Fault.builder().faultCode("500").faultString("找不到对应网站的处理器").build()).build());
    }

}
