package top.leftblue.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Snapshot;
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
        System.out.println("1111111111111111");
        System.out.println(newExtension.getClass().getSimpleName());
        if (newExtension instanceof Snapshot snapshot) {
            System.out.println("2222222222");
            System.out.println(snapshot.getSpec().getContentPatch());
            System.out.println(snapshot.getSpec().getRawPatch());
            publishService.publish(snapshot);
        }
    }
}
