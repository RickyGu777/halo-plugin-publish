package top.leftblue.publish.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.content.Post;
import top.leftblue.publish.halo.ContentWrapper;
import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.service.Publisher;

@RequiredArgsConstructor
@Component
public class NonePublisher implements Publisher {

    @Override
    public Mono<MethodResponse> publish(Post post, ContentWrapper content) {
        return Mono.just(MethodResponse.builder().fault(MethodResponse.Fault.builder().faultCode("500").faultString("找不到对应网站的处理器").build()).build());
    }

    @Override
    public Mono<MethodResponse> edit(Post post, ContentWrapper content, String postId) {
        return Mono.just(MethodResponse.builder().fault(MethodResponse.Fault.builder().faultCode("500").faultString("找不到对应网站的处理器").build()).build());
    }

}
