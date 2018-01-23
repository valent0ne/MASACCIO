package it.univaq.masaccio.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;


public class KafkaConsumeEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumeEventPublisher.class);

    @Override
    public void setApplicationEventPublisher (ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishKafkaConsumeEvent(String message) {
        KafkaConsumeEvent event = new KafkaConsumeEvent(this, message);
        LOGGER.info("event triggered");
        publisher.publishEvent(event);
        LOGGER.info("event published");
    }

}
