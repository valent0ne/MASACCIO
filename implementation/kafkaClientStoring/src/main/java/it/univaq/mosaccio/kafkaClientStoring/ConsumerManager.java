package it.univaq.mosaccio.kafkaClientStoring;

import it.univaq.mosaccio.kafkaClientStoring.dao.implementation.MosaccioDaoMongoDBImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.implementation.MosaccioDaoMySQLImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.interfaces.MosaccioDaoMongoDB;
import it.univaq.mosaccio.kafkaClientStoring.dao.interfaces.MosaccioDaoMySQL;
import it.univaq.mosaccio.kafkaClientStoring.model.Area;
import static it.univaq.mosaccio.kafkaClientStoring.Main.*;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ConsumerManager {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerManager.class);

    private KafkaConsumer<String, String> consumer;
    private Properties properties;
    private MosaccioDaoMongoDB mongo;
    private MosaccioDaoMySQL mysql;

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
        this.mysql = new MosaccioDaoMySQLImpl();

    }

    /**
     * subscribe to the topics returned by getAreas()
     */
    public void subscribe(){
        List<String> topics = getAreas();

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


    /**
     * retrieves the area list from the mysql db and return the names
     */
    private List<String> getAreas() {
        List<String> out = new ArrayList<>();
        try {
            this.mysql.init(); // to init the connection with sql DB
            List<Area> l = this.mysql.getAreas();
            LOGGER.info("fetched areas");
            for (Area a : l) {
                LOGGER.info("area name: " + a.getName());
                out.add(a.getName());
            }
            this.mysql.close(); // to close the connection
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return out;
    }

    /**
     * consumes all the data from all the subscribed topics
     * @param pollSize size of the single poll
     */
    public void consume(Integer pollSize){
        long startTime = System.currentTimeMillis();
        try {
            mongo.init();
            while(true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                if (estimatedTime > REFRESH_TIME){
                    throw new TimeoutException("timeout reached, refreshing...");
                }
                // poll return a list of records: Each record contains the topic and partition the record came from
                ConsumerRecords<String, String> records = this.consumer.poll(pollSize);
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info("consumed record: (topic = {}, partition = {}, offset = {}, key = {}, value = {})\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    mongo.insert(record.value(), record.topic());
                    // TODO check autocommit
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
