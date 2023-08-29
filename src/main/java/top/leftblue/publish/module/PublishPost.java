package top.leftblue.publish.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@GVK(group = "plugin.leftblue.top",
        version = "v1alpha1",
        kind = "PublishPost",
        plural = "publishposts",
        singular = "publishpost")
public class PublishPost extends AbstractExtension {

    @Schema(description = "Post name in halo")
    private String name;

    List<SitePost> sitePosts;

    @Data
    public static class SitePost {
        @Schema(description = "Site name")
        private String site;

        @Schema(description = "Postid in other blog site")
        private String postid;

        @Schema(description = "title in other blog site")
        private String title;
    }


}