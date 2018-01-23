package it.univaq.masaccio.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class KafkaConsumeEventHandler implements ApplicationListener<KafkaConsumeEvent> {

    private final SimpMessagingTemplate template ;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumeEventHandler.class);

    public KafkaConsumeEventHandler(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onApplicationEvent(final KafkaConsumeEvent event) {
        LOGGER.info("event detected: publishing...");
        LOGGER.info("message to be published: {}",event.getMessage());
        template.convertAndSend("/topic/messages", event.getMessage());
        LOGGER.info("event published to websocket.");

    }

    @Bean
    public KafkaConsumeEventHandler kafkaConsumeEventHandler(SimpMessagingTemplate messagingTemplate) {
        KafkaConsumeEventHandler handler = new KafkaConsumeEventHandler(messagingTemplate);
        return handler;
    }

}
