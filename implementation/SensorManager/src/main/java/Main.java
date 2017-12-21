import KafkaProducer.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static String result;


    public static void main(String[] args) {
        // initialize the kafka producer
        KafkaProducerManager p = new KafkaProducerManager();
        while(true) {

            //sense the world
            result = p.sense();

            //publish the data on kafka
            p.send(result);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
