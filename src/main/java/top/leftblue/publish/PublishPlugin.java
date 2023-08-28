package top.leftblue.publish;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;

/**
 * @author 佐蓝
 * @since 1.0.0
 */
@Slf4j
@Component
public class PublishPlugin extends BasePlugin {

    public PublishPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
