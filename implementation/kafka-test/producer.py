from kafka import KafkaProducer
import time


def main():

    producer = KafkaProducer(bootstrap_servers='localhost:9092')
    for i in range(40000):
        msg = "{'test': 'mongo'}"
        producer.send('a1.1', bytes(str(msg), 'UTF-8'))
        producer.send('a1.2', bytes(str(msg), 'UTF-8'))
        producer.send('a1.3', bytes(str(msg), 'UTF-8'))
        producer.send('a1.4', bytes(str(msg), 'UTF-8'))
        producer.send('a1.5', bytes(str(msg), 'UTF-8'))
        producer.send('a1.6', bytes(str(msg), 'UTF-8'))
        print("send msg: {}".format(msg))
    time.sleep(1)


if __name__ == "__main__":
    main()
