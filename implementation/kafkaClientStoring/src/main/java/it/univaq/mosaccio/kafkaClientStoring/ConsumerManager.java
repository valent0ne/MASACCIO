package it.univaq.mosaccio.kafkaClientStoring;

import it.univaq.mosaccio.kafkaClientStoring.dao.implementation.MosaccioDaoMongoDBImpl;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class ConsumerManager {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private KafkaConsumer<String, String> consumer;
    private Properties properties;
    private MosaccioDaoMongoDBImpl mongo;

    public ConsumerManager(String address, String groupId){
        // create new properties kafka object
        this.properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        // we set here the group id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // we set here values about the deserializer
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.connect.json.JsonDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");


        this.consumer = new KafkaConsumer<>(properties);
        this.mongo = new MosaccioDaoMongoDBImpl();

    }

    public void subscribe(List<String> topics){
        // SUBSCRIBE TO ALL THE TOPICS
        try {
            LOGGER.info("subscribing...");
            this.consumer.subscribe(topics);
            LOGGER.info("subscribed");

        } catch (KafkaException e) {
            LOGGER.error("Cannot subscribe to topics - {}", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }
        }
    }

    public void consume(Integer pollSize){
        // POLL FOR TO READ DATA

        try {
            mongo.init();
            while(true) {
                // poll return a list of records: Each record contains the topic and partition the record came from
                ConsumerRecords<String, String> records = this.consumer.poll(pollSize);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("consumed record: (topic = {}, partition = {}, offset = {}, key = {}, value = {})\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    mongo.insert(record.value(), record.topic());
                    // COMMIT DEFAULT TO TRUE: IF WE MANAGE IT WILL BE BETTER IN ORDER TO AVOID LOSS OF DATA (SYNCRONOUS OR ASYNCRONOUS??)
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception in record consumption - {}", e.getMessage());

        } finally{
            consumer.close();
            try {
                mongo.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                if (LOGGER.isDebugEnabled()){
                    e.printStackTrace();
                }
            }
        }
    }


}
