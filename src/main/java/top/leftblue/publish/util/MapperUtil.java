package top.leftblue.publish.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import top.leftblue.publish.metaweblog.annotation.MWAElementStruct;
import top.leftblue.publish.metaweblog.annotation.MWAElementWrapper;
import top.leftblue.publish.metaweblog.annotation.MWARootElement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MapperUtil {

    public static boolean isHtml(String text) {
        try {
            DocumentHelper.parseText(text);
            return true;
        } catch (DocumentException e) {
            return false;
        }
    }

    public static String markdown2Html(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private static final List<String> valueElement =
            List.of("methodName", "string", "int", "boolean");

    public static <T> T xml2Bean(String xml, Class<T> clz) throws Exception {
        Document document = DocumentHelper.parseText(xml);
        Element element = document.getRootElement();
        T object = clz.getDeclaredConstructor().newInstance();
        return element2Bean(element, object, null);
    }

    private static <T> T element2Bean(Element element, T object, Integer index) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < element.elements().size(); i++) {
            if (fields.length < i + 1) {
                break;
            }
            Element child = element.elements().get(i);
            if (valueElement.contains(child.getName())) {
                setFieldValue(fields[index == null ? i : index], object, child.getText().trim());
            } else if ("struct".equals(child.getName())) {
                Field structField = getStructField(fields);
                if (structField == null) {
                    continue;
                }
                Class<?> type = structField.getType();
                Object struct = buildStruct(child, type);
                setFieldValue(structField, object, struct);
            } else if ("params".equals(child.getName())) {
                Object childObject = fields[i].getType().getDeclaredConstructor().newInstance();
                element2Bean(child, childObject, null);
                setFieldValue(fields[i], object, childObject);
            } else {
                element2Bean(child, object, index == null ? i : index);
            }
        }
        return object;
    }

    private static Field getStructField(Field[] fields) {
        for (Field field : fields) {
            if (field.getAnnotation(MWAElementStruct.class) != null) {
                return field;
            }
        }
        return null;
    }

    private static <T> T buildStruct(Element structElement, Class<T> clz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException, NoSuchFieldException {
        T object = clz.getDeclaredConstructor().newInstance();
        for (Element member : structElement.elements()) {
            Field memberField = clz.getDeclaredField(member.element("name").getText().trim());
            setFieldValue(memberField, object,
                    member.element("value").elements().get(0).getText().trim());
        }
        return object;
    }

    private static void setFieldValue(Field field, Object obj, Object val)
            throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            field.set(obj, "true".equals(val.toString()) || "1".equals(val.toString()));
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.set(obj, Integer.valueOf(val.toString()));
        } else {
            field.set(obj, val);
        }
        field.setAccessible(false);
    }

    private static final List<String> notValueElement =
            List.of("methodName");

    public static String bean2Xml(Object obj) throws Exception {
        Document document = DocumentHelper.createDocument();
        MWARootElement rootName = obj.getClass().getAnnotation(MWARootElement.class);
        Element rootElement = document.addElement(rootName.value());
        bean2Element(rootElement, obj, false);
        return rootElement.getDocument().asXML();
    }

    private static void bean2Element(Element element, Object obj, boolean isStruct) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            Object value = getValue(field, obj);
            if (value == null) {
                return;
            }
            Class<?> fieldType = field.getType();
            MWAElementWrapper wrapper = field.getAnnotation(MWAElementWrapper.class);
            String childName = wrapper == null ? field.getName() : wrapper.value();
            if (wrapper != null) {
                bean2Element(element.addElement(wrapper.value()), value, false);
            } else if (fieldType == String.class || fieldType == Boolean.class || fieldType == boolean.class || fieldType == Integer.class || fieldType == int.class) {
                if (notValueElement.contains(childName)) {
                    element.addElement(childName).addText(value.toString());
                } else if (isStruct) {
                    Element member = element.addElement("member");
                    member.addElement("name").addText(childName);
                    member.addElement("value").addElement("string").addText(value.toString());
                } else {
                    element.addElement("param").addElement("value").addElement("string").addText(value.toString());
                }
            } else {
                Element struct = element.addElement("param").addElement("value").addElement("struct");
                bean2Element(struct, value, true);
            }
        }
    }

    private static Object getValue(Field field, Object obj) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(obj);
        field.setAccessible(false);
        return value;
    }

}
