package top.leftblue.publish.http;

import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

@Component
public class MeatWeblogApi {

    public String doMethod(String host, String methodXml) {
        return HttpUtil.post(host, methodXml, 5000);
    }

}
