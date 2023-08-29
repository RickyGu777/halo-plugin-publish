package top.leftblue.publish;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.leftblue.publish.metaweblog.server.MetaWebLogServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Component
@AllArgsConstructor
public class PublishEndpoint {

    private final MetaWebLogServer metaWebLogServer;

    @Bean
    RouterFunction<ServerResponse> sitemapRouterFunction() {
        return RouterFunctions.route(POST("/publish")
                .and(accept(MediaType.TEXT_XML)), request -> {
                return request.bodyToMono(String.class)
                    .switchIfEmpty(
                        Mono.error(new RuntimeException("Empty xml content is not allowed"))
                    )
                    .flatMap(metaWebLogServer::convertContent)
                    .flatMap(metaWebLogServer::publish)
                    .then(ServerResponse.ok().contentType(MediaType.TEXT_XML).bodyValue("test"));
            }
        );
    }

}
