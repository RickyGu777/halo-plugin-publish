package top.leftblue.publish.metaweblog.module;

import lombok.Data;
import top.leftblue.publish.metaweblog.annotation.MWARootElement;

@Data
@MWARootElement("methodCall")
public class MethodCall {

    private String methodName;

}
