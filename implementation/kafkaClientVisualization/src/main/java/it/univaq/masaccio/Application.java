package it.univaq.masaccio;

import it.univaq.masaccio.event.KafkaConsumeEventPublisher;
import it.univaq.masaccio.kafka.ConsumerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);


        //run main (kafka client)
        try {
            Thread.sleep(3000);

        }catch (Exception e){

        }

        Main.main(args);

    }
}
