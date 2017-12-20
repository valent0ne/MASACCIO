package it.univaq.mosaccio.kafkaClientStoring;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.univaq.mosaccio.kafkaClientStoring.Utils.*;

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

    //kafka consumer poll size
    @Parameter(names = {"--refresh_time", "-refresh"}, description = "topic list refresh time (ms)")
    public static Integer REFRESH_TIME = 30 * 1000; //30 seconds

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
     * main function
     */
    private void run() {
        //create the consumermanager object specifying address and group
        ConsumerManager c = new ConsumerManager(KAFKA_ADDRESS, KAFKA_GROUP);
        // after REFRESH_TIME the consumption will be broken and then restarted
        while(true){
            //subscribe to all the area topics
            c.subscribe();
            //start consumption
            c.consume(KAFKA_POLL_SIZE);
        }


    }


}