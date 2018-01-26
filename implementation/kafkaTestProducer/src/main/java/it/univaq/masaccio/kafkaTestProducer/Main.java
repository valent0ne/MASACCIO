package it.univaq.masaccio.kafkaTestProducer;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static it.univaq.masaccio.kafkaTestProducer.Utils.readProperties;
import static it.univaq.masaccio.kafkaTestProducer.Utils.setLoggingLevel;

public class Main {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params
    @Parameter(names = {"--log", "-l"}, description = "log level")
    private static String LOG_LEVEL = "info";

    //configuration file
    @Parameter(names = {"--config", "-c"}, description = "configuration file")
    public static String CONFIG_FILE = "config.properties";

    public static Properties properties;


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar <name>.jar");

        try {
            jc.parse(args);
            LOGGER.info("setting logging level {}...", LOG_LEVEL.toUpperCase());
            setLoggingLevel(LOG_LEVEL);

            LOGGER.info("loading properties from {}...", CONFIG_FILE);
            properties = readProperties(CONFIG_FILE);

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


    private void run() throws ExecutionException, InterruptedException {
        // initialize the kafka producer

        int messages_per_topic = Integer.parseInt(Main.properties.getProperty("messages_per_topic"));
        Producer p = new Producer();
        long startTime = System.nanoTime();
        int delta = Integer.parseInt(Main.properties.getProperty("delta"));
        LOGGER.info("publishing...");
        for(int i=1; i<=messages_per_topic + delta; i++) {
            //LOGGER.info("publishing round: {}", i);
            //publish the data on kafka
            p.send(p.sense());
        }
        long elapsedTime = System.nanoTime() - startTime;
        double seconds = (double)elapsedTime / 1000000000.0;
        LOGGER.info("published {} messages to {} topics", messages_per_topic, properties.getProperty("topics_number"));
        LOGGER.info("elapsed time: {}", seconds);

    }
}
