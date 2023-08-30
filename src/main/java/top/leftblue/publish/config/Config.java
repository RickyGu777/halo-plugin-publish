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

    public Mono<BasicConfig> getBasicConfig() {
        return settingFetcher.fetch("basic", BasicConfig.class);
    }

    public Mono<MetaWeblogConfig> getMetaWeblogConfig(String group) {
        return settingFetcher.fetch(group, MetaWeblogConfig.class);
    }

}
