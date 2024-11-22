package sample.demosse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController {


    private final SseEmitters sseEmitter;
    private final SseEmitters sseEmitters;


    @GetMapping(value = "/connect/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@PathVariable Long userId){
        SseEmitter emitter = new SseEmitter(60*30000L);

        sseEmitters.add(emitter, userId);
        try{
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("helloConnected!"));
        }catch (Exception e){
            log.error("Error occurred while sending message to client", e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/friendRequest/{requestId}/{toId}")
    public ResponseEntity<Void> count(@PathVariable Long requestId,@PathVariable Long toId){
        sseEmitters.sendMsg(requestId,toId);
        return ResponseEntity.ok().build();
    }



}
