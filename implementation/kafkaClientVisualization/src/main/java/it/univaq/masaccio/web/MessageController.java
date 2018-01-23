package it.univaq.masaccio.web;

import it.univaq.masaccio.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(path = "/messages", method = POST)
    public void send(Message msg) {
        CommService.send(msg);
    }
}
