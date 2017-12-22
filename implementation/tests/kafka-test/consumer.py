from kafka import KafkaConsumer
import time


def main():
    consumer = KafkaConsumer(bootstrap_servers='localhost:9092')
    consumer.subscribe(['a1.7', 'a1.8', 'a1.9', 'a1.10', 'a1.11', 'a1.12'])
    start = time.time()
    i = 1
    while True:
        consumer.poll(100)
        for msg in consumer:
            #print("received msg {}: {}".format(i, msg.value))
            i = i +1
            if i > 240000:
                end = time.time() - start
                print(end)
                exit()




if __name__ == "__main__":
    main()
