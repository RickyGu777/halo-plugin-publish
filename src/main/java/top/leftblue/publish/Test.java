package top.leftblue.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import top.leftblue.publish.dto.MWAResult;
import top.leftblue.publish.util.MapperUtil;

public class Test {

    public static void main(String[] args) {
        String xml = """
                <?xml version="1.0" encoding="utf-8"?>
                <methodResponse>
                  <fault>
                    <value>
                      <struct>
                        <member>
                          <name>faultCode</name>
                          <value>
                            <int>500</int>
                          </value>
                        </member>
                        <member>
                          <name>faultString</name>
                          <value>
                            <string>相同标题的博文已存在</string>
                          </value>
                        </member>
                      </struct>
                    </value>
                  </fault>
                </methodResponse>""";
        try {
            MWAResult mwaResult = MapperUtil.xmlStr2Bean(xml, MWAResult.class);
            System.out.println(mwaResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
