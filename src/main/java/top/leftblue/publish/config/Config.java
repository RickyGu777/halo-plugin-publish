package top.leftblue.publish.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.SettingFetcher;

@RequiredArgsConstructor
@Component
public class Config {

    private final SettingFetcher settingFetcher;

    public BasicConfig getBasicConfig() {
        return settingFetcher.fetch("basic", BasicConfig.class).orElse(null);
    }

    public MetaWeblogConfig getMetaWeblogConfig(String group) {
        return settingFetcher.fetch(group, MetaWeblogConfig.class).orElse(null);
    }

}
