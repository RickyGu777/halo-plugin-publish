package top.leftblue.publish.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.leftblue.publish.service.Publisher;
import top.leftblue.publish.service.impl.CnBlogPublisher;

@AllArgsConstructor
public enum Platform {
    CNBLOG("cnblog", "博客园", CnBlogPublisher.class);

    @Getter
    private final String name;

    @Getter
    private final String label;

    @Getter
    private final Class<? extends Publisher> handleClz;

    public static Platform from(String name) {
        for (Platform value : Platform.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
