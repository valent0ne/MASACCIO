from kafka import KafkaConsumer
import time


def main():
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092')
    consumer.subscribe(['a1.1', 'a1.2', 'a1.3', 'a1.4', 'a1.5', 'a1.6'])
    while True:
        consumer.poll(100)
        for msg in consumer:
            print("received msg: {}".format(msg.value))
        time.sleep(0.1)


if __name__ == "__main__":
    main()
