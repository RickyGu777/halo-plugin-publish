package top.leftblue.publish;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import run.halo.app.core.extension.content.Post;
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
        System.out.println(newExtension.getClass().getSimpleName());
        if (newExtension instanceof Post post) {
            System.out.println("更新了 Post");
            System.out.println(post);
        }
        if (newExtension instanceof Snapshot snapshot) {
            System.out.println("更新了 Snapshot");
            System.out.println(snapshot);
        }
    }
}
