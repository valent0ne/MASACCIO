package it.univaq.mosaccio.kafkaClientStoring;


import ch.qos.logback.classic.Level;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params
    @Parameter(names = {"--log", "-l"}, description = "log level"/*, required = true*/)
    private static String LOG_LEVEL = "INFO";

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
    private void run(){
        LOGGER.debug("DEBUG: ON");
        System.out.println("Hello world");
    }

    private static void setLoggingLevel(String level) {
        Level l = Level.toLevel(level);
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(l);
    }
}