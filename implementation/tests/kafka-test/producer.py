from kafka import KafkaProducer
import time


def main():
    start= time.time()
    msg = "{'test': 'mongo00'}"
    producer = KafkaProducer(bootstrap_servers='localhost:9092')
    for i in range(40000):
        producer.send('a1.7', bytes(str(msg), 'UTF-8'))
        producer.send('a1.8', bytes(str(msg), 'UTF-8'))
        producer.send('a1.9', bytes(str(msg), 'UTF-8'))
        producer.send('a1.10', bytes(str(msg), 'UTF-8'))
        producer.send('a1.11', bytes(str(msg), 'UTF-8'))
        producer.send('a1.12', bytes(str(msg), 'UTF-8'))
        #print("send msg: {}".format(msg))
    end = time.time() - start
    print(end)


if __name__ == "__main__":
    main()
