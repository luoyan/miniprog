import time
import os
import sys 
import pymongo
import karait
if pymongo.version.startswith("2.5"):
    import bson.code
    pymongo.code = bson.code
    sys.modules['pymongo.code'] = bson.code
from karait import Message
from karait import Queue
help(Queue)
