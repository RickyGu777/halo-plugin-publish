package top.leftblue.publish.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.leftblue.publish.publisher.Publisher;
import top.leftblue.publish.publisher.impl.CnBlogPublisher;
import top.leftblue.publish.publisher.impl.NonePublisher;

@AllArgsConstructor
public enum Platform {
    CNBLOG("cnblog", "博客园", CnBlogPublisher.class),
    NONE("none", "空平台", NonePublisher.class),
    ;
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
        return NONE;
    }

}
