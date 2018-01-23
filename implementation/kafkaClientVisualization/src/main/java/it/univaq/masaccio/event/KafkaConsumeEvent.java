package it.univaq.masaccio.event;

import org.springframework.context.ApplicationEvent;

public class KafkaConsumeEvent extends ApplicationEvent {

    private String message;

    public KafkaConsumeEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
