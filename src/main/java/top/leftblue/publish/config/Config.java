package top.leftblue.publish.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import run.halo.app.plugin.ReactiveSettingFetcher;
import run.halo.app.plugin.SettingFetcher;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class Config {

    private final ReactiveSettingFetcher settingFetcher;
    private final SettingFetcher fetcher;

    public Mono<BasicConfig> getBasicConfig() {
        return settingFetcher.fetch("basic", BasicConfig.class);
    }

    public Mono<MetaWeblogConfig> getMetaWeblogConfig(String group) {
        return settingFetcher.fetch(group, MetaWeblogConfig.class);
    }

    public Optional<BasicConfig> getBasicConfig2() {
        return fetcher.fetch("basic", BasicConfig.class);
    }

    public Optional<MetaWeblogConfig> getMetaWeblogConfig2(String group) {
        return fetcher.fetch(group, MetaWeblogConfig.class);
    }

}
