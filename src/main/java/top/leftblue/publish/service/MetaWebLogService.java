package top.leftblue.publish.service;

import reactor.core.publisher.Mono;
import top.leftblue.publish.dto.MWACmd;

public interface MetaWebLogService {

    Mono<MWACmd> convertContent(String bodyXml);

    Mono<String> publish(MWACmd mwaCmd);

}
