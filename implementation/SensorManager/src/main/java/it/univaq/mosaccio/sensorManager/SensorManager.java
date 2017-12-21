package it.univaq.mosaccio.sensorManager;

//import util.properties packages
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.univaq.mosaccio.sensorManager.Main.*;

import java.util.Random;

public class SensorManager {
    private final Properties kafkaProps;
    private KafkaProducer<String, String> producer;
    private String topicName = KAFKA_TOPIC;

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorManager.class);

    public SensorManager(){
        // kafka properties attribute
        this.kafkaProps = new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_ADDRESS);
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<>(kafkaProps);
    }


    public String sense(){
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String message = "{ 'id':'"+SENSOR_ID+"', 'value':'"+salt.toString()+"', 'timestamp':'"+timestamp+"'}";
        LOGGER.info("payload: {}", message);
        return message;
    }

    public void send(String data){
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topicName, null, data);
        try {
            LOGGER.info("publishing message on topic {} ...", KAFKA_TOPIC);
            producer.send(record);
            LOGGER.info("message published");
        }
        catch (KafkaException e) {
            LOGGER.error("Exception while publishing - {}", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }
    }
}
