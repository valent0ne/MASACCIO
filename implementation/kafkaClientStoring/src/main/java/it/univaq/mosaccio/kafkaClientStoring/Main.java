package it.univaq.mosaccio.kafkaClientStoring;


import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import it.univaq.mosaccio.kafkaClientStoring.dao.exception.DaoException;
import it.univaq.mosaccio.kafkaClientStoring.dao.implementation.MosaccioDaoMongoDBImpl;
import it.univaq.mosaccio.kafkaClientStoring.dao.implementation.MosaccioDaoMySQLImpl;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.json.simple.JSONObject;

//import util.properties packages
import java.util.Properties;
//import KafkaProducer packages
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;


import it.univaq.mosaccio.kafkaClientStoring.model.*;
import it.univaq.mosaccio.kafkaClientStoring.dao.*;

import java.util.List;

public class Main {
    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params
    @Parameter(names = {"--log", "-l"}, description = "log level"/*, required = true*/)
    private static String LOG_LEVEL = "INFO";

    //mysql db username
    @Parameter(names = {"--mysql_user", "-my_usr"}, description = "mysql db username")
    public static String MYSQL_USER = "root";

    //mysql db password
    @Parameter(names = {"--mysql_psw", "-my_psw"}, description = "mysql db password")
    public static String MYSQL_PSW = "root";

    //mysql db host:port
    @Parameter(names = {"--mysql_addr", "-my_addr"}, description = "mysql db address (host:port)")
    public static String MYSQL_ADDR = "localhost:3306";

    //mysql db name
    @Parameter(names = {"--mysql_db", "-my_db"}, description = "mysql db name")
    public static String MYSQL_DB = "mosaccio";

    //mongodb username
    @Parameter(names = {"--mongo_user", "-mo_usr"}, description = "mongodb username")
    public static String MONGO_USER = "";

    //mongodb password
    @Parameter(names = {"--mongo_psw", "-mo_psw"}, description = "mongodb password")
    public static String MONGO_PSW = "";

    //mongodb db host:port
    @Parameter(names = {"--mongo_addr", "-mo_addr"}, description = "mongodb address (host:port)")
    public static String MONGO_ADDR = "localhost:27017";

    //mongodb db name
    @Parameter(names = {"--mongo_db", "-mo_db"}, description = "mongodb db name")
    public static String MONGO_DB = "mosaccio";


    /**
     * main - init of logging framework
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar <name>.jar");

        try{
            jc.parse(args);
            setLoggingLevel(LOG_LEVEL);

        }catch (Exception e){
            //if there is something wrong print the cli usage instructions
            jc.usage();

            LOGGER.debug(e.getMessage());
            if(LOGGER.isDebugEnabled()){
                e.printStackTrace();
            }

            System.exit(1);
        }
        main.run();

    }

    /**
     * main function
     */
    private void run() {
        LOGGER.debug("DEBUG: ON");
        System.out.println("Hello world");

        // create new properties kafka object
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        // we set here the group id
        props.put("group.id", "KafkaStoring");
        // we set here values about the deserializer
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        MosaccioDaoMySQLImpl m = new MosaccioDaoMySQLImpl();
        List<String> temp = new ArrayList<>();
        try {
            m.init(); // to init the connection with sql DB
            List<Area> l = m.getAreas();
            for (Area a : l) {
                LOGGER.info("area id: " + a.getId());
                temp.add(a.getId());
            }
            m.destroy(); // to close the connection
        } catch (DaoException e) {
            LOGGER.error(e.getMessage());
        }

        // SUBSCRIBE TO ALL THE TOPICS
        try {
            consumer.subscribe(temp);

        } catch (KafkaException e) {

            // TODO: 19/12/2017
        }

        // POLL FOR TO READ DATA
        try {

            while(true) {
                // poll return a list of records: Each record contains the topic and partition the record came from
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumer Record:(topic = %s, partition = %s offset= %s, key = %s, value = %s, )\n",record.topic(), record.partition(), record.offset(), record.key(), record.value());

                    // COMMIT DEFAULT TO TRUE: IF WE MANAGE IT WILL BE BETTER IN ORDER TO AVOID LOSS OF DATA (SYNCRONOUS OR ASYNCRONOUS??)
                   }
                }
            } finally{
                consumer.close();
            }

        }


    /**
     * utility function - changes the logging level given the level as a string
     * @param level String containing the logging level
     */
    private static void setLoggingLevel(String level) {
        Level l = Level.toLevel(level);
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(l);
    }
}