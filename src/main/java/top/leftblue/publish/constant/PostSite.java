package top.leftblue.publish.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.leftblue.publish.publisher.Publisher;
import top.leftblue.publish.publisher.impl.CnBlogPublisher;
import top.leftblue.publish.publisher.impl.NonePublisher;

@AllArgsConstructor
public enum PostSite {
    CNBLOG("cnblog", "博客园", CnBlogPublisher.class),
    NONE("none", "空", NonePublisher.class),
    ;
    @Getter
    private final String name;

    @Getter
    private final String label;

    @Getter
    private final Class<? extends Publisher> handleClz;

    public static PostSite from(String name) {
        for (PostSite value : PostSite.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return NONE;
    }

}
