"""
QoS
At most once (0)
At least once (1)
Exactly once (2)
"""
import paho.mqtt.client as mqtt
import time


def on_message(client, userdata, message):
    print("Received message '" + str(message.payload) + "' on topic '" + message.topic + "' with QoS " + str(message.qos))


client = mqtt.Client()  # connection
client.connect("LOCALHOST", 1883, 60)
client.loop_start()
i = 0
now = time.clock()
while i < 40000:
    #print(i)  # number of messages received
    client.on_message = on_message
    client.subscribe('prova', 0)  # the second parameter is the Qos
    i = 1+i

now1 = time.clock()
print(now1-now)

"""
Non so se effettivamente riceve 40.000 messaggi...
posso provare anche con from paho.mqtt import subscribe
"""