package top.leftblue.publish;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.BasePlugin;
import top.leftblue.publish.module.PublishPost;

/**
 * @author 佐蓝
 * @since 1.0.0
 */
@Slf4j
@Component
public class PublishPlugin extends BasePlugin {

    private final SchemeManager schemeManager;
    private final ReactiveExtensionClient client;
    private final PostWatcher postWatcher;

    public PublishPlugin(SchemeManager schemeManager, PluginWrapper wrapper, ReactiveExtensionClient client, PostWatcher postWatcher) {
        super(wrapper);
        this.schemeManager = schemeManager;
        this.client = client;
        this.postWatcher = postWatcher;
    }

    @Override
    public void start() {
//        client.watch(postWatcher);
        schemeManager.register(PublishPost.class);
//        HaloPluginManager pluginManager = (HaloPluginManager) wrapper.getPluginManager();
    }

    @Override
    public void stop() {
    }
}
