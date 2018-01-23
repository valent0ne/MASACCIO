package it.univaq.masaccio.web;

import it.univaq.masaccio.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    // publishes returned message to defined topic (by @SendTo directive)
    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public static String send(String msg) throws Exception {
        return msg;
    }
}
