import os
import sys 
import logging
import logging.config
import pymongo
from pymongo import Connection
if pymongo.version.startswith("2.5"):
    import bson.code
    pymongo.code = bson.code
    sys.modules['pymongo.code'] = bson.code
from karait import Message, Queue

#queue = Queue()
queue = Queue(
        host='localhost', # MongoDB host. Defaults to localhost.
        port=2006, # MongoDB port. Defaults to 27017.
        database='karait', # Database that will store the karait queue. Defaults to karait.
        queue='messages', # The capped collection that karait writes to. Defaults to messages.
        average_message_size=8192, # How big do you expect the messages will be in bytes? Defaults to 8192.
        queue_size=4096 # How many messages should be allowed in the queue. Defaults to 4096.
        )

message = queue.read()[0]
print "%s" % (message.name)

message.delete()

# or
message = queue.read(routing_key='my_routing_key')[0]
print "%s" % (message.action)

message.delete()
