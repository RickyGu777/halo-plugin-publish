package top.leftblue.publish.http;


import lombok.Data;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface HaloApi {

    @GetExchange("/login/public-key")
    PublicKeyResponse getPublicKey();


    @Data
    class PublicKeyResponse {

        private String base64Format;

    }
}
