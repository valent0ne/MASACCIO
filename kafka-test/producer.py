from kafka import KafkaProducer
import time


def main():
    producer = KafkaProducer(bootstrap_servers='localhost:2181')
    for i in range(40000):
        msg = "lolo22_{}".format(i)
        producer.send('my-topic', bytes(str(msg), 'UTF-8'))
        print("send msg: {}".format(msg))
    time.sleep(500)


if __name__ == "__main__":
    main()
