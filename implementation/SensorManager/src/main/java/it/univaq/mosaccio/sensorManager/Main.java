package it.univaq.mosaccio.sensorManager;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static it.univaq.mosaccio.sensorManager.Utils.setLoggingLevel;

public class Main {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params
    @Parameter(names = {"--log", "-l"}, description = "log level")
    private static String LOG_LEVEL = "info";

    //mysql db username
    @Parameter(names = {"--kafka_topic", "-k_t"}, description = "kafka topic name")
    public static String KAFKA_TOPIC = "";

    //kafka cluster address
    @Parameter(names = {"--kafka_addr", "-k_addr"}, description = "kafka cluster address (host:port)")
    public static String KAFKA_ADDRESS = "localhost:9092";


    //kafka cluster address
    @Parameter(names = {"--sensor_id", "-s_id"}, description = "device identifier")
    public static String SENSOR_ID = "999";


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


    private void run() {
        // initialize the kafka producer
        SensorManager p = new SensorManager();
        while (true) {

            //publish the data on kafka
            p.send(p.sense());

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
