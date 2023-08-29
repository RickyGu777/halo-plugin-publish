package top.leftblue.publish.metaweblog.module;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;


@Data
@FieldNameConstants
public class MethodPost {

    private String title;
    private String description;
    private LocalDateTime dateCreated;
    private List<String> categories;
    private Enclosure enclosure;
    private String link;
    private String permalink;
    private String postid;
    private Source source;
    private String userid;
    private String mt_allow_comments;
    private String mt_allow_pings;
    private String mt_convert_breaks;
    private String mt_text_more;
    private String mt_excerpt;
    private String mt_keywords;
    private String wp_slug;


}
