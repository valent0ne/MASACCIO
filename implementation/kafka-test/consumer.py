from kafka import KafkaConsumer
import time


def main():
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092',
                             auto_offset_reset='earliest')
    consumer.subscribe(['my-topic-andrea'])
    consumer.poll()
    while True:
        for msg in consumer:
            print("received msg: {}".format(msg.value))
        time.sleep(0.1)


if __name__ == "__main__":
    main()
