package top.leftblue.publish;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import run.halo.app.security.AdditionalWebFilter;
import top.leftblue.publish.service.BlogPublishService;

import java.util.List;

@Component
public class PostHandler implements AdditionalWebFilter {

    private final BlogPublishService blogService;

    private final ServerWebExchangeMatcher matcher =
            ServerWebExchangeMatchers.pathMatchers(HttpMethod.PUT,
                    "/apis/api.console.halo.run/v1alpha1/posts/*/publish");

    public PostHandler(BlogPublishService blogService) {
        this.blogService = blogService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println(exchange.getRequest().getPath().value());
        return matcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(matchResult -> render(exchange));
    }

    private Mono<Void> render(ServerWebExchange exchange) {
        new Thread(() -> {
            List<String> cookie = exchange.getRequest().getHeaders().get("Cookie");
            String postName = exchange.getRequest().getPath().elements().get(9).value();
            blogService.publish(postName, cookie.get(0));
        }).start();
        return Mono.empty();
    }

    @Override
    public int getOrder() {
        return AdditionalWebFilter.super.getOrder();
    }

}