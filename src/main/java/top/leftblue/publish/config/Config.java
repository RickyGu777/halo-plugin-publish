package top.leftblue.publish.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.SettingFetcher;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class Config {

    private final SettingFetcher fetcher;

    public Optional<BasicConfig> getBasicConfig() {
        return fetcher.fetch("basic", BasicConfig.class);
    }

    public Optional<MetaWeblogConfig> getMetaWeblogConfig(String group) {
        return fetcher.fetch(group, MetaWeblogConfig.class);
    }

}
