package it.univaq.masaccio.kafkaTestConsumer;



import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;


public class ConsumerManager {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerManager.class);

    private KafkaConsumer<String, String> consumer;
    private Properties properties;


    public ConsumerManager(String address, String groupId){
        // create new properties kafka object
        this.properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        // we set here the group id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, ""+System.nanoTime());
        // disable the autocommit
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // we set here values about the deserializer
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");


        this.consumer = new KafkaConsumer<>(properties);


    }

    /**
     * subscribe to the topics returned by getAreas()
     */
    public void subscribe(){
        List<String> topics = new ArrayList<>();
        int topics_number = Integer.parseInt(Main.properties.getProperty("topics_number"));
        for(int i=1; i<= topics_number; i++){
            topics.add("topic_"+i);
        }

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
     * consumes all the data from all the subscribed topics
     * @param pollSize size of the single poll
     */
    public void consume(Integer pollSize){
        // take the time
        long startTime = 0;
        int message = 0;
        int topics_number = Integer.parseInt(Main.properties.getProperty("topics_number"));
        int total_messages = topics_number*Integer.parseInt(Main.properties.getProperty("messages_per_topic"));
        try {
            while(true) {
                // poll return a list of records: Each record contains the topic and partition the record came from
                ConsumerRecords<String, String> records = this.consumer.poll(pollSize);
                for (ConsumerRecord<String, String> record : records) {
                    message += 1;
 

                    if (message == 1){
                        startTime = System.nanoTime();
                    }
                    if (message >= total_messages){
                        LOGGER.info("consumed a total of {} messages from {} topics", total_messages, topics_number);
                        closeTest(System.nanoTime() - startTime);
                    }



                    // in order to say "ok, we saved"
                    // consumer.commitSync();
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception in record consumption - {}", e.getMessage());
            exit(1);

        } finally{
            consumer.close();
        }
    }

    public void closeTest(long elapsedTime){
        long max = 0;

        double seconds = (double)elapsedTime / 1000000000.0;

        LOGGER.info("elapsed time: {} seconds", seconds);
        exit(0);
    }


}
