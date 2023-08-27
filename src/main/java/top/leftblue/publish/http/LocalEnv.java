package top.leftblue.publish.http;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@RequiredArgsConstructor
@Component
@Data
public class LocalEnv {

    private String host;
    private String port;

    public static String HALO_SERVICE_ADDRESS;

    @PostConstruct
    public void setEnv() {
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
            System.out.println("host :   " + host);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("port :   " + port);
        HALO_SERVICE_ADDRESS = "http://" + host + ":" + port;

    }


}
