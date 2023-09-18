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
public class MethodEditResponse {

    @MWAElementWrapper("params")
    private MethodResult result;

    @MWAElementStruct
    private Fault fault;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MethodResult {
        private boolean success;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fault {
        private int faultCode;
        private String faultString;
    }

    public boolean isFailed() {
        return this.fault != null;
    }

}
