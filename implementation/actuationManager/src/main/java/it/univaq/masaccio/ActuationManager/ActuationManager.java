package it.univaq.masaccio.ActuationManager;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ActuationManager {


    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ActuationManager.class);

    private KafkaConsumer<String, String> consumer;
    private Properties properties;


    public ActuationManager(String address, String groupId){
        // create new properties kafka object
        this.properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        // we set here the group id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // we set here values about the deserializer
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        this.consumer = new KafkaConsumer<>(properties);

    }




    /**
     * subscribe to the topics 
     */
    public void subscribe(String topic){

        try {
            LOGGER.info("subscribing...");
            this.consumer.subscribe(Collections.singletonList(topic));
            LOGGER.info("subscribed");

        } catch (KafkaException e) {
            LOGGER.error("Cannot subscribe to topics - {}", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }
    }

    public void consume(Integer pollSize){
        //TODO


        try {
            // if there isn't the mongo connection will fail and will not consume the data.
            while(true) {
                // poll return a list of records: Each record contains the topic and partition the record came from
                ConsumerRecords<String, String> records = this.consumer.poll(pollSize);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("consumed record: (topic = {}, partition = {}, offset = {}, key = {}, value = {})\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    // in order to say "ok, we saved"
                    // do something
                    this.actuation(record.value());
                    consumer.commitAsync();
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception in record consumption - {}", e.getMessage());

        } finally{
            consumer.close();
            }
        }



    public void actuation(String value){
        LOGGER.info("Actuator triggered by value: {}", value);
        //TODO do something
    }
}
