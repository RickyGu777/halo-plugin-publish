package top.leftblue.publish;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import top.leftblue.publish.module.Commend;
import top.leftblue.publish.module.NewPost;

public class Test {

    public static void main(String[] args) throws Exception {
        String xml = """
            <methodCall>
              <methodName>
                metaWeblog.newPost
              </methodName>
              <params>
                <param>
                  <value>
                    <string>
                      1
                    </string>
                  </value>
                </param>
                <param>
                  <value>
                    <string>
                      ricky_grk@163.com
                    </string>
                  </value>
                </param>
                <param>
                  <value>
                    <string>
                      36129341DF84A28B63AEB80608D0A5A5E50C3784A2B841EF3ECA0496F8FC5E99
                    </string>
                  </value>
                </param>
                <param>
                  <value>
                    <struct>
                      <member>
                        <name>
                          title
                        </name>
                        <value>
                          <string>
                            test title2
                          </string>
                        </value>
                      </member>
                      <member>
                        <name>
                          description
                        </name>
                        <value>
                          <string>
                            test content
                          </string>
                        </value>
                      </member>
                    </struct>
                  </value>
                </param>
                <param>
                  <value>
                    <string>
                      true
                    </string>
                  </value>
                </param>
              </params>
            </methodCall>""";
        Commend commend = xml2Bean2(xml, Commend.class);
        System.out.println(commend);

    }

    private static final List<String> valueElement =
        List.of("methodName", "string", "int", "boolean");

    private static <T> T xml2Bean2(String xml, Class<T> clz) throws Exception {
        Document document = DocumentHelper.parseText(xml);
        Element element = document.getRootElement();
        T object = clz.getDeclaredConstructor().newInstance();
        return element2Bean(element, object, null);
    }

    private static <T> T element2Bean(Element element, T object, Integer index) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < element.elements().size(); i++) {
            Element child = element.elements().get(i);
            if (valueElement.contains(child.getName())) {
                setValue(fields[index == null ? i : index], object, child.getText().trim());
            } else if ("struct".equals(child.getName())) {
                Class<?> type = fields[index == null ? i : index].getType();
                Object struct = buildStruct(child, type);
                setValue(fields[index == null ? i : index], object, struct);
            } else if ("params".equals(child.getName())) {
                Object childObject = fields[i].getType().getDeclaredConstructor().newInstance();
                element2Bean(child, childObject, null);
                setValue(fields[i], object, childObject);
            } else {
                element2Bean(child, object, index == null ? i : index);
            }
        }
        return object;
    }

    private static <T> T buildStruct(Element structElement, Class<T> clz)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException,
        IllegalAccessException, NoSuchFieldException {
        T object = clz.getDeclaredConstructor().newInstance();
        for (Element member : structElement.elements()) {
            Field memberField = clz.getDeclaredField(member.element("name").getText().trim());
            setValue(memberField, object,
                member.element("value").element("string").getText().trim());
        }
        return object;
    }

    private static void setValue(Field field, Object obj, Object val)
        throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            field.set(obj, Boolean.valueOf(val.toString()));
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.set(obj, Integer.valueOf(val.toString()));
        } else {
            field.set(obj, val);
        }
        field.setAccessible(false);
    }


}
