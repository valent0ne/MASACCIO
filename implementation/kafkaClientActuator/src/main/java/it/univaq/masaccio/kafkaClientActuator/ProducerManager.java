package it.univaq.masaccio.kafkaClientActuator;

//import util.properties packages

import java.util.Properties;

//import KafkaProducer packages
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;



    public class ProducerManager {
        private final Properties kafkaProps;
        private KafkaProducer<String, String> producer;

        //logger
        private static final Logger LOGGER = LoggerFactory.getLogger(ProducerManager.class);

        public ProducerManager(){
            // kafka properties attribute
            this.kafkaProps = new Properties();
            kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Main.properties.getProperty("kafka_address"));
            kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
            kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
            // The Producer config property retries defaults to 0 and is the retry count if Producer does not get an ack from Kafka Broker LEADER.
            // Set the number of retries
            kafkaProps.put(ProducerConfig.RETRIES_CONFIG, 5);

            // ALL THIS TO MANAGE MORE "BROKER": REPLICHE DEL LEADER

            // Setting this to 1 guarantee that messages will be written to the broker in the order in
            // which they were sent, even when retries occur.
            kafkaProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);
            // We have to receive the leader ack in order to know that the message was written in a right way to the .
            //kafkaProps.put(ProducerConfig.ACKS_CONFIG, "1");
            this.producer = new KafkaProducer<>(kafkaProps);
        }


        public void send(String t, String data) throws ExecutionException, InterruptedException {
            // This method add the record ot the output buffer. By default is asynchronous.
            ProducerRecord<String, String> record = new ProducerRecord<>(t, null, data);
            try {
                LOGGER.info("publishing message on topic {} ...", t);
                //  The send method is asynchronous and returns right away as soon as the record gets added to the send buffer.
                producer.send(record, new ProducerCallback());
                LOGGER.info("message published");
            }
            catch (KafkaException e) {
                LOGGER.error("Exception while publishing - {}", e.getMessage());
                if (LOGGER.isDebugEnabled()){
                    e.printStackTrace();
                }
            }
        }
    }

    class ProducerCallback implements Callback {

        //logger
        private static final Logger LOGGER = LoggerFactory.getLogger(ProducerCallback.class);

        /* To use callbacks, we need a class that implements the org.apache.kafka. clients.producer.Callback interface,
        which has a single function—onComple tion().
         */
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            //The callback gets invoked when the broker has acknowledged the send.
            if (e != null) { // if the exception is not-null

                //TODO MANAGING EXCEPTION
                // direi di inviare su un altro broker..
                e.printStackTrace();

            }
            else {
                LOGGER.info("Triggering message sent.");
            }
        } }

