import sys
import os
import datetime
from pymongo import ASCENDING  
from pymongo import Connection
MONGODB_HOST = 'app.maimiaotech.com'
MONGODB_PORT = 2007
_conn = Connection(host = MONGODB_HOST, port = MONGODB_PORT)
_db = 'CommonInfo'
shop_status = _conn[_db]['shop_status']
last_day = datetime.datetime.combine(datetime.date.today(), datetime.time()) - datetime.timedelta(days=0)
'''
query = { '$and' :[
        {'key_campaign_ids':{'$exists':True}}
        #,{'key_campaign_init_success':True}
        #,{'key_campaign_settings':{'$exists':True}}
        #,{'$or':[{'key_campaign_cancel_status':{'$exists':False}}, {'key_campaign_cancel_status':False}]}
        ,{'$or':[{'drawback_status':{'$exists':False}}, {'drawback_status':False}]}
        ,{'session_expired':False}
        ,{'insuff_level':False}
        ,{'$or':[{'key_campaign_optimize_time':{'$exists':False}}, {'key_campaign_optimize_time':{'$lt':last_day}}]}
        ]
        }
'''
query = { '$and' :[
        {'key_campaign_ids':{'$exists':True}}
        #,{'key_campaign_init_success':True}
        #,{'key_campaign_settings':{'$exists':True}}
        #,{'$or':[{'key_campaign_cancel_status':{'$exists':False}}, {'key_campaign_cancel_status':False}]}
        #,{'$or':[{'drawback_status':{'$exists':False}}, {'drawback_status':False}]}
        #,{'session_expired':False}
        #,{'insuff_level':False}
        #,{'$or':[{'key_campaign_optimize_time':{'$exists':False}}]}
        ]
        }
cursor = shop_status.find(query).sort('key_campaign_optimize_time', ASCENDING)
shops = [ element for element in cursor]
print "shops = " + str(shops)
print "name = " + shops[0].__class__.__name__
#print "len(shops) = " + str(len(shops))
#lst = [1, 5, 3, 8, 2]
#lst2 = lst[0:100]
#print lst2
#lst3 = lst[0:0]
#print lst3
