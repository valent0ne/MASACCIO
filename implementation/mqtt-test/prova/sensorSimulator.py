"""
QoS
At most once (0)
At least once (1)
Exactly once (2)
"""

import paho.mqtt.client as mqtt


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))


seq = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0]
client = mqtt.Client()  # connection
client.on_connect = on_connect
client.connect("LOCALHOST", 1883, 60)
client.loop_start()


client.publish('prova', 'hhh', 0)  # the third parameter is the QoS

i = 0
input('premi enter')
while i < 40000:
    client.publish('prova', i, 0)  # the third parameter is the QoS
    i = i + 1





