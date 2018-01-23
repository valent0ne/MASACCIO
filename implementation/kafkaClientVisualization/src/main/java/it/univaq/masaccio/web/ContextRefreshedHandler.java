package it.univaq.masaccio.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ContextRefreshedHandler implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedHandler.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            //Initialize the template for web socket messages
            CommService.setTemplate(template);
        } catch (Exception ex) {
            LOGGER.error("exception in event consumption: {}", ex.getMessage());
            if(LOGGER.isDebugEnabled()){
                ex.printStackTrace();
            }
        }
    }
}
