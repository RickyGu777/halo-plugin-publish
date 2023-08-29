package top.leftblue.publish.metaweblog.module;

import lombok.Data;
import top.leftblue.publish.metaweblog.annotation.MWAElementStruct;
import top.leftblue.publish.metaweblog.annotation.MWAElementWrapper;
import top.leftblue.publish.metaweblog.annotation.MWARootElement;

@Data
@MWARootElement("methodResponse")
public class MethodResponse {

    @MWAElementWrapper("params")
    private MethodResult result;

    @MWAElementStruct
    private Fault fault;

    @Data
    public static class MethodResult {
        private String postid;
    }

    @Data
    public static class Fault {
        private String faultCode;
        private String faultString;
    }

}
