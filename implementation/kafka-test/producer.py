from kafka import KafkaProducer
import time


def main():

    producer = KafkaProducer(bootstrap_servers='localhost:9092')
    for i in range(400000):
        msg = "lolo22_{}".format(i)
        producer.send('roio', bytes(str(msg), 'UTF-8'))
        producer.send('coppito_0', bytes(str(msg), 'UTF-8'))
        producer.send('coppito_1', bytes(str(msg), 'UTF-8'))
        producer.send('coppito_2', bytes(str(msg), 'UTF-8'))
        producer.send('medicina', bytes(str(msg), 'UTF-8'))
        producer.send('economia', bytes(str(msg), 'UTF-8'))
        print("send msg: {}".format(msg))
    time.sleep(5)


if __name__ == "__main__":
    main()
