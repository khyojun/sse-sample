package sample.demosse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
public class SseEmitters {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    SseEmitter add(SseEmitter emitter, Long userId ){
        this.emitters.put(userId, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion CallBack : 완료되었습니다.");
            this.emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout CallBack : 시간이 초과되었습니다.");
            emitter.complete();
        });
        return emitter;
    }

    public void sendMsg(Long requestUserId, Long toUserId){
        System.out.println(requestUserId);
        System.out.println(toUserId);
        System.out.println(emitters.toString());
        SseEmitter emitter = this.emitters.get(toUserId);
        try{
            String format = String.format("친구요청이 해당 %d를 가진 사람에게 왔습니다! 수락?", requestUserId);
            emitter.send(SseEmitter.event()
                    .name("friendRequest")
                    .data(format)
            );

        }catch (Exception e){
            log.error("Error occurred while sending message to client", e);
            throw new RuntimeException(e);
        }


    }

//    public void count(){
//        long count = counter.incrementAndGet();
//        emitters.forEach(emitter -> {
//            try{
//                emitter.send(SseEmitter.event()
//                        .name("count")
//                        .data(count));
//            }catch (Exception e){
//                log.error("Error occurred while sending message to client", e);
//                throw new RuntimeException(e);
//            }
//        });
//    }



}
