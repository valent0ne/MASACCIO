package it.univaq.masaccio.kafkaClientStoring;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

public class Utils {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);


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

    /**
     * read properties from configuration file
     *
     * @param fileName String name of the file containing the properties
     */
    public static Properties readProperties(String fileName){
        Properties prop = new Properties();
        try{
            Path p = Paths.get(fileName);
            BufferedReader br = Files.newBufferedReader(p);
            prop.load(br);
            br.close();

            Enumeration enuKeys = prop.keys();
            LOGGER.info("loaded properties:");
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = prop.getProperty(key);
                LOGGER.info(key + "=" + value);
            }
        } catch (Exception e) {
            LOGGER.error("exception while loading properties - {}", e.getMessage());
            if(LOGGER.isDebugEnabled()){
                e.printStackTrace();

            }
            return readProperties("target/"+fileName);

        }
        return prop;
    }
}
