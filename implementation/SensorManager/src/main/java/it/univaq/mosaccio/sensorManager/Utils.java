package it.univaq.mosaccio.sensorManager;

import ch.qos.logback.classic.Level;

public class Utils {


    /**
     * utility function - changes the logging level given the level as a string
     *
     * @param level String containing the logging level
     */
    public static void setLoggingLevel(String level) {
        Level l = Level.toLevel(level);
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(l);
    }
}
