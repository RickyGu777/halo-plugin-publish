package top.leftblue.publish;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.SettingFetcher;
import top.leftblue.publish.config.BasicConfig;
import top.leftblue.publish.metaweblog.server.MetaWebLogServer;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Slf4j
@Component
@AllArgsConstructor
public class PublishEndpoint {

    private final MetaWebLogServer metaWebLogServer;
    private final SettingFetcher settingFetcher;

    @Bean
    RouterFunction<ServerResponse> sitemapRouterFunction() {
        Optional<BasicConfig> basic = settingFetcher.fetch("basic", BasicConfig.class);
        if (basic.isEmpty()) {
            return null;
        }
        if (!basic.get().getAllowMetaWeblogAccess()) {
            return null;
        }
        log.debug("halo-plugin-publish: Start MetaWeblog service");
        String path = StringUtils.isBlank(basic.get().getMetaWeblogPath()) ? "/xmlrpc" :
                basic.get().getMetaWeblogPath();
        log.debug("halo-plugin-publish: service path is '{}'", path);
        return RouterFunctions.route(POST(path).and(accept(MediaType.TEXT_XML)), request -> {
            return request.bodyToMono(String.class)
                    .flatMap(s -> {
                        log.debug("halo-plugin-publish: receive body '{}'", s);
                        return Mono.just(s);
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("Empty xml content is not allowed")))
                    .flatMap(metaWebLogServer::handleMethodCall)
                    .flatMap(response -> ServerResponse.ok().contentType(MediaType.TEXT_XML).bodyValue(response));
        });
    }

}
