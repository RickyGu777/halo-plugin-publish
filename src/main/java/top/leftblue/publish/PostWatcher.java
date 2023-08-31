package top.leftblue.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.Setting;
import run.halo.app.extension.Extension;
import run.halo.app.extension.Watcher;
import top.leftblue.publish.service.PublishService;

@RequiredArgsConstructor
@Component
public class PostWatcher implements Watcher {

    private final PublishService publishService;

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return Watcher.super.isDisposed();
    }

    @Override
    public void onUpdate(Extension oldExtension, Extension newExtension) {
        if (newExtension instanceof Setting setting) {
        }
    }
}
