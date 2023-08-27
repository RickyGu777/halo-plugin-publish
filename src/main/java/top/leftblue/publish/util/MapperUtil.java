package top.leftblue.publish.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MapperUtil {

    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String bean2XmlStr(Object o) throws JsonProcessingException {
        return xmlMapper.writeValueAsString(o);
    }

    public static <T> T xmlStr2Bean(String xml, Class<T> clz) throws JsonProcessingException {
        return xmlMapper.readValue(xml, clz);
    }

    public static <T> T jsonStr2Bean(String value, Class<T> clz) throws JsonProcessingException {
        return objectMapper.readValue(value, clz);
    }

}
