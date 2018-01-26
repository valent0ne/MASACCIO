package it.univaq.masaccio.kafkaTestProducer;

//import util.properties packages
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ExecutionException;

public class Producer {
    private final Properties kafkaProps;
    private KafkaProducer<String, String> producer;

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    public Producer(){
        // kafka properties attribute
        this.kafkaProps = new Properties();
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
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "all");
        this.producer = new KafkaProducer<>(kafkaProps);

    }


    public String sense(){
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.SSS").format(new Date());
        return "{ 'timestamp':'"+timestamp+"' }";
        // LOGGER.info("payload: {}", message);
        //return message;
    }

    public void send(String data) throws ExecutionException, InterruptedException {
        // This method add the record ot the output buffer. By default is asynchronous.
        int topics_number = Integer.parseInt(Main.properties.getProperty("topics_number"));
        //ProducerRecord<String, String> record = new ProducerRecord<>(this.topicName, null, data);
        try {
            //LOGGER.info("publishing message on {} topics...", topics_number);
            //  The send method is asynchronous and returns right away as soon as the record gets added to the send buffer.
            for (int i = 1; i <= topics_number; i++) {
                producer.send(new ProducerRecord<>("topic_" + i, null, data));
            }
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