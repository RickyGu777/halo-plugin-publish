package top.leftblue.publish.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JianShuApi {

    interface ApiPath {
        String NEW_POST = "https://www.jianshu.com/author/notes";
        String UPDATE_CONTENT = "https://www.jianshu.com/author/notes/{}";
        String PUBLISH = "https://www.jianshu.com/author/notes/{}/publicize";
        String PRIVATIZE = "https://www.jianshu.com/author/notes/{}/privatize";
    }
}
