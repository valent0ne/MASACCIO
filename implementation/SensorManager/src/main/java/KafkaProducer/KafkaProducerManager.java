package KafkaProducer;

//import util.properties packages
import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;

import java.util.Random;

public class KafkaProducerManager {
    private final Properties kafkaProps;
    private KafkaProducer<String, String> producer;
    private String topicName = "Coppito_0";

    public KafkaProducerManager(){
        // kafka properties attribute
        this.kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<String, String>(kafkaProps);

    }


    public String sense(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public void send(String data){
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topicName, null, data);
        try {
            producer.send(record);
        }
        catch (KafkaException e) {
            e.printStackTrace();
        }
    }
}
