package top.leftblue.publish.halo;

import io.swagger.v3.oas.annotations.media.Schema;
import run.halo.app.core.extension.content.Post;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record PostRequest(@Schema(requiredMode = REQUIRED) Post post,
                          @Schema(requiredMode = REQUIRED) Content content) {

    public record Content(String raw, String content, String rawType) {
    }
}