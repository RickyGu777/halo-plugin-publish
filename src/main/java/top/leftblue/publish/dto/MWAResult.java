package top.leftblue.publish.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "methodResponse")
public class MWAResult {

    @JacksonXmlElementWrapper(localName = "params")
    @JacksonXmlProperty(localName = "param")
    private List<Param> params;

    @JacksonXmlElementWrapper(localName = "fault")
    private Fault fault;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Param {

        @JacksonXmlElementWrapper(localName = "value")
        private Value value;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fault {

        @JacksonXmlElementWrapper(localName = "value")
        private Value value;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Value {
        @JacksonXmlProperty(localName = "string")
        private String strValue;

        @JacksonXmlProperty(localName = "int")
        private String intValue;

        @JacksonXmlProperty(localName = "boolean")
        private String boolValue;

        @JacksonXmlElementWrapper(localName = "struct")
        @JacksonXmlProperty(localName = "member")
        private List<Member> members;

        public String getStrValue() {
            return strValue == null || strValue.length() == 0 ? intValue : strValue;
        }

        public Boolean getBoolValue() {
            return boolValue == null ? null : ("true".equals(boolValue) || "1".equals(boolValue));
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Member {
        @JacksonXmlProperty(localName = "name")
        private String strName;

        @JacksonXmlElementWrapper(localName = "value")
        @JacksonXmlProperty(localName = "value")
        private NameValue strValue;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NameValue {
        @JacksonXmlProperty(localName = "string")
        private String strValue;

        @JacksonXmlProperty(localName = "int")
        private String intValue;
    }
}
