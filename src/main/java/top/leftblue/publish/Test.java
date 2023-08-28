package top.leftblue.publish;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.InputSource;
import top.leftblue.publish.dto.MWAResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

public class Test {

    public static void main(String[] args) throws DocumentException {
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

        Class<MWAResult> clz = MWAResult.class;
        MWAResult mwaResult = null;
        try {
            mwaResult = clz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        for (int i = 0; i < root.elements().size(); i++) {
            Element element = root.elements().get(i);
            // 处理结构体
            if ("struct".equals(element.getName())){
                if ("param".equals(element.getParent().getParent().getName())){

                }
            }
        }
        for (Element element : root.elements()) {

        }
        System.out.println(document.getRootElement().elements().get(0).elements());
    }


}
