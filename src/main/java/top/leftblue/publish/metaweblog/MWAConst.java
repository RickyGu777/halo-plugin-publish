package top.leftblue.publish.metaweblog;

public interface MWAConst {

    String getPost = "metaWeblog.getPost";
    String getRecentPosts = "metaWeblog.getRecentPosts";
    String getUsersBlogs = "blogger.getUsersBlogs";
    String newPost = "metaWeblog.newPost";
    String editPost = "metaWeblog.editPost";
    String deletePost = "blogger.deletePost";
    String getCategories = "metaWeblog.getCategories";
    String newMediaObject = "metaWeblog.newMediaObject";
    String newCategory = "wp.newCategory";

    String INTERNAL_ERROR = """
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
                        <string>服务器内部错误</string>
                      </value>
                    </member>
                  </struct>
                </value>
              </fault>
            </methodResponse>""";

    String METHOD_NOT_SUPPORT = """
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
                        <string>不支持的方法</string>
                      </value>
                    </member>
                  </struct>
                </value>
              </fault>
            </methodResponse>""";
}
