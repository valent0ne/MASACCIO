package it.univaq.masaccio.sensorManager;

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


import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SensorManager {
    private final Properties kafkaProps;
    private KafkaProducer<String, String> producer;
    private String topicName = Main.properties.getProperty("kafka_topic");

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(SensorManager.class);

    public SensorManager(){
        // kafka properties attribute
        this.kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, Main.properties.getProperty("sensor_id"));
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Main.properties.getProperty("kafka_address"));
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        // The Producer config property retries defaults to 0 and is the retry count if Producer does not get an ack from Kafka Broker LEADER.
        // Set the number of retries
        kafkaProps.put(ProducerConfig.RETRIES_CONFIG, 5);

        // ALL THIS TO MANAGE MORE "BROKER": REPLICHE DEL LEADER

        // Setting this to 1 guarantee that messages will be written to the broker in the order in
        // which they were sent, even when retries occur.
        kafkaProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
        // We have to receive the leader ack in order to know that the message was written in a right way to the .
        //kafkaProps.put(ProducerConfig.ACKS_CONFIG, "1");
        this.producer = new KafkaProducer<>(kafkaProps);
    }


    public String sense(){
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String message = "{ 'id':'"+Main.properties.getProperty("sensor_id")+"', 'value':'"+salt.toString()+"', 'timestamp':'"+timestamp+"'}";
        LOGGER.info("payload: {}", message);
        return message;
    }

    public void send(String data) throws ExecutionException, InterruptedException {
        // This method add the record ot the output buffer. By default is asynchronous.
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topicName, null, data);
        try {
            LOGGER.info("publishing message on topic {} ...", Main.properties.getProperty("kafka_topic"));
            //  The send method is asynchronous and returns right away as soon as the record gets added to the send buffer.
            producer.send(record, new ProducerCallback());
        }
        catch (KafkaException e) {
            LOGGER.error("Exception while publishing - {}", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }
    }
}

class ProducerCallback implements Callback {


    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerCallback.class);

    /* To use callbacks, we need a class that implements the org.apache.kafka. clients.producer.Callback interface,
    which has a single function—onComple tion().
     */
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        //The callback gets invoked when the broker has acknowledged the send.
        if (e != null) { // if the exception is not-null

            //TODO MANAGING EXCEPTION
            // direi di inviare su un altro broker..
            e.printStackTrace();

        }
        else {
            LOGGER.info("message published");
        }
    } }