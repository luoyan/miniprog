#! /usr/bin/env python
# -*- coding: utf-8 -*-
import os,sys
import datetime
debug = True
if debug :
    MONGODB_HOST = 'app.maimiaotech.com'
    MONGODB_PORT = 2007
else:
    MONGODB_HOST = '223.5.20.246'
    MONGODB_PORT = 1990

from pymongo import Connection
try:
    mongoConn = Connection(host = MONGODB_HOST, port = MONGODB_PORT)
#    mongoConn2 = Connection(host = MONGODB_HOST, port = 2006)
except Exception:
    mongoConn = None
import logging
import logging.config
logging.config.fileConfig('./syb_test.conf')
logger = logging.getLogger(__name__)
from celery import Celery
import celeryconfig2
celery_rabbitmq = Celery()
celery_rabbitmq.config_from_object(celeryconfig2)
#mongoConn2
#col2 = mongoConn2['queryall']['query_key']
#query = '泰拳'
#query = '榨水车'
#cursor = col2.find_one({'_id':query})
#print "curser = " + str(cursor)
#import pdb
#pdb.set_trace()
def syb_test(shop_status):
    logger.info("log shop_status['nick'] = " + shop_status['nick'])
    print "print shop_status[nick] = " + shop_status['nick']

@celery_rabbitmq.task(name='syb_test_wrapper')
def syb_test_wrapper(shop_status):
    syb_test(shop_status)

def get_list():
    last_day = datetime.datetime.combine(datetime.date.today(), datetime.time()) - datetime.timedelta(days=1)
    query = {'$and':[
            {'auto_campaign_ids':{'$exists':True}}
            ,{'$or':[{'drawback_status':{'$exists':False}}, {'drawback_status':False}]}
            ,{'session_expired':False}
            ,{'insuff_level':False}
            ,{'$or':[{'auto_campaign_optimize_time':{'$exists':False}}, {'auto_campaign_optimize_time':{'$lt':last_day}}]}
            ]  
            }
    _db = 'CommonInfo'
    shop_status_db = mongoConn[_db]['shop_status']
    cursor = shop_status_db.find(query)
    shops = [ element for element in cursor]
    return shops
def send(shop_status):
    result = syb_test_wrapper.apply_async([shop_status], countdown=0)
    logger.info("log send to celery shop_status['nick'] = " + shop_status['nick'])
    print "send to celery [" + shop_status['nick'] + " ]"
 
if __name__ == '__main__':
    shop_status_list = get_list()
    shop_status_list = shop_status_list[:10]
    for shop_status in shop_status_list:
        send(shop_status)
