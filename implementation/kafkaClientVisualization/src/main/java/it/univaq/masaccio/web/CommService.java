package it.univaq.masaccio.web;

import it.univaq.masaccio.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class CommService {

    private static Logger LOGGER = LoggerFactory.getLogger(CommService.class);

    private static SimpMessagingTemplate template;

    public static void setTemplate(SimpMessagingTemplate tmplt) {
        template = tmplt;
    }

    public static void send(Message msg) {
        LOGGER.info("converting and sending...");
        template.convertAndSend("/topic/messages/"+msg.getAreaName(), msg);
        LOGGER.info("sent");
    }
}
