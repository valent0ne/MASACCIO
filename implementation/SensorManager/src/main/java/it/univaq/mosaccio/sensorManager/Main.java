package it.univaq.mosaccio.sensorManager;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static it.univaq.mosaccio.sensorManager.Utils.readProperties;
import static it.univaq.mosaccio.sensorManager.Utils.setLoggingLevel;

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
