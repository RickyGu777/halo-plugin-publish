package top.leftblue.publish.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.leftblue.publish.service.impl.CnBlogPublisherImpl;

@AllArgsConstructor
public enum Platform {
    CNBLOG("cnblog", "博客园", CnBlogPublisherImpl.class);

    @Getter
    private final String name;

    @Getter
    private final String label;

    @Getter
    private final Class<?> handleClz;

    public Platform from(String name) {
        for (Platform value : Platform.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
