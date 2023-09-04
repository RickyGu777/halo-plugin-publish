package top.leftblue.publish.metaweblog.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.leftblue.publish.metaweblog.annotation.MWAElementStruct;
import top.leftblue.publish.metaweblog.annotation.MWAElementWrapper;
import top.leftblue.publish.metaweblog.annotation.MWARootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MWARootElement("methodResponse")
public class MethodResponse {

    @MWAElementWrapper("params")
    private MethodResult result;

    @MWAElementStruct
    private Fault fault;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MethodResult {
        private String postid;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fault {
        private String faultCode;
        private String faultString;
    }

    public boolean isFailed(){
        return this.fault != null && this.fault.getFaultCode() != null;
    }

}
