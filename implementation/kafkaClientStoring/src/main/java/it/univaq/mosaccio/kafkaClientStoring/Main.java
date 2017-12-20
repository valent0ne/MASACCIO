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

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import it.univaq.mosaccio.kafkaClientStoring.model.*;

import java.util.List;

public class Main {
    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params
    @Parameter(names = {"--log", "-l"}, description = "log level"/*, required = true*/)
    private static String LOG_LEVEL = "info";

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
    public static String MONGO_ADDR = "192.168.0.25:27017";

    //mongodb db name
    @Parameter(names = {"--mongo_db", "-mo_db"}, description = "mongodb db name")
    public static String MONGO_DB = "mosaccio";

    //kafka cluster address
    @Parameter(names = {"--kafka_addr", "-k_addr"}, description = "kafka cluster address (host:port)")
    public static String KAFKA_ADDRESS = "localhost:9092";

    //kafka consumer group id
    @Parameter(names = {"--kafka_group", "-k_group"}, description = "kafka group id")
    public static String KAFKA_GROUP = "storing";

    //kafka consumer poll size
    @Parameter(names = {"--kafka_poll_size", "-k_poll_size"}, description = "kafka poll size")
    public static Integer KAFKA_POLL_SIZE = 100;

    /**
     * main - init of logging framework
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar <name>.jar");

        try {
            jc.parse(args);
            setLoggingLevel(LOG_LEVEL);

        } catch (Exception e) {
            //if there is something wrong print the cli usage instructions
            jc.usage();

            LOGGER.debug(e.getMessage());
            if (LOGGER.isDebugEnabled()) {
                e.printStackTrace();
            }

            System.exit(1);
        }
        main.run();

    }

    /**
     * retrieves the area list from the mysql db and return the names
     */
    private List<String> getAreas() {
        MosaccioDaoMySQLImpl m = new MosaccioDaoMySQLImpl();
        List<String> out = new ArrayList<>();
        try {
            m.init(); // to init the connection with sql DB
            List<Area> l = m.getAreas();
            LOGGER.info("fetched areas");
            for (Area a : l) {
                LOGGER.info("area name: " + a.getName());
                out.add(a.getName());
            }
            m.destroy(); // to close the connection
        } catch (DaoException e) {
            LOGGER.error(e.getMessage());
        }
        return out;
    }


    /**
     * utility function - changes the logging level given the level as a string
     *
     * @param level String containing the logging level
     */
    private static void setLoggingLevel(String level) {
        Level l = Level.toLevel(level);
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(l);
    }

    /**
     * main function
     */
    private void run() {

        ConsumerManager c = new ConsumerManager(KAFKA_ADDRESS, KAFKA_GROUP);
        c.subscribe(getAreas());
        c.consume(KAFKA_POLL_SIZE);

    }


}