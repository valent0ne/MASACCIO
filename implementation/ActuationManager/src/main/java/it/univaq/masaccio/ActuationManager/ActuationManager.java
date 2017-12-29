package it.univaq.masaccio.ActuationManager;

import it.univaq.masaccio.ActuationManager.dao.implementation.MasaccioDaoMongoDBImpl;
import it.univaq.masaccio.ActuationManager.dao.implementation.MasaccioDaoMySQLImpl;
import it.univaq.masaccio.ActuationManager.dao.interfaces.MasaccioDaoMongoDB;
import it.univaq.masaccio.ActuationManager.dao.interfaces.MasaccioDaoMySQL;
import it.univaq.masaccio.ActuationManager.model.Area;
import org.apache.commons.lang3.ObjectUtils;
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

public class ActuationManager {


    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ActuationManager.class);

    private KafkaConsumer<String, String> consumer;
    private Properties properties;
    private MasaccioDaoMongoDB mongo;
    private MasaccioDaoMySQL mysql;

    public ActuationManager(String address, String groupId){
        // create new properties kafka object
        this.properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        // we set here the group id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // we set here values about the deserializer
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        this.mongo = new MasaccioDaoMongoDBImpl();
        this.mysql = new MasaccioDaoMySQLImpl();

    }




    /**
     * subscribe to the topics 
     */
    public void subscribe(List topic){

        try {
            LOGGER.info("subscribing...");
            this.consumer.subscribe(topic);
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

        // do something
        this.actuation();

    }

    public void actuation(){
        //TODO do something
    }
}
