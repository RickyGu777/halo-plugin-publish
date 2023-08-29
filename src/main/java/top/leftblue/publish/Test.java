package top.leftblue.publish;

import top.leftblue.publish.metaweblog.module.MethodResponse;
import top.leftblue.publish.util.MapperUtil;

public class Test {

    public static void main(String[] args) throws Exception {
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
</methodResponse>
""";
//        MethodCall methodCall = new MethodCall();
//        methodCall.setMethodName("metaWeblog.methodContent");
//        MethodContent methodContent = new MethodContent();
//        methodCall.setMethodContent(methodContent);
//        methodContent.setBlogid("111");
//        methodContent.setUsername("111");
//        methodContent.setPassword("111");
//        MWAPost mwaPost = new MWAPost();
//        methodContent.setMwaPost(mwaPost);
//        mwaPost.setTitle("title");
//        mwaPost.setDescription("description");
//        methodContent.setPublish(true);
        MethodResponse methodResponse = MapperUtil.xml2Bean(xml, MethodResponse.class);
        System.out.println(methodResponse);
    }


}
