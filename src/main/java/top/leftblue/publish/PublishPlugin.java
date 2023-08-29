package top.leftblue.publish;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
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

    private SchemeManager schemeManager;

    public PublishPlugin(SchemeManager schemeManager, PluginWrapper wrapper) {
        super(wrapper);
        this.schemeManager = schemeManager;
    }

    @Override
    public void start() {
        schemeManager.register(PublishPost.class);
    }

    @Override
    public void stop() {
    }
}
