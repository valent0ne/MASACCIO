package it.univaq.masaccio.ActuationManager;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    public static void main(String[] args) {
        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar <name>.jar");

        try {
            jc.parse(args);
            LOGGER.info("setting logging level {}...", LOG_LEVEL.toUpperCase());
            Utils.setLoggingLevel(LOG_LEVEL);

            LOGGER.info("loading properties from {}...", CONFIG_FILE);
            properties = Utils.readProperties(CONFIG_FILE);

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
        ActuationManager c = new ActuationManager(properties.getProperty("kafka_address"), properties.getProperty("kafka_group"));
        // after refresh_time the consumption will be broken and then restarted
        while(true){
            //subscribe to all the area topics
            c.subscribe(properties.getProperty("kafka_topic"));
            //start consumption
            c.consume(Integer.parseInt(properties.getProperty("kafka_poll_size")));
        }
    }
}
