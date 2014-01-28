#!/usr/bin/python
#encoding=utf8
__author__ = 'luoyan@maimiaotech.com'
import sys 
import os 
import logging 
import logging.config
logger = logging.getLogger(__name__)
from jiapu.settings import mongoConn

class Table(object):
    """
    class to operate cold_query  
    """
    _conn = mongoConn
    def __init__(self, db, coll, uniq_key = 'id'):
        self._db = db
        self._coll = coll
        self._coll_new = coll + '_new'
        self.uniq_key = uniq_key
        self.coll = self._conn[self._db][self._coll]
        self.coll_new = self._conn[self._db][self._coll_new]

    def scan(cls, batch_size=30):
        try:
            cursor = cls.coll.find().batch_size(batch_size)
        except Exception,e:
            logger.debug('scan error : %s'%(e))
            return None
        return cursor

    def query(cls, id):
        try:
            cursor = cls.coll.find_one({'id': id})
        except Exception,e:
            logger.debug('query error : %s'%(e))
            return None
        return cursor

    def save(cls, item, is_new=False):
        try:
            if not is_new:
                cls.coll.update({cls.uniq_key:item[cls.uniq_key]}, item, True)
            else:
                cls.coll_new.update({cls.uniq_key:item[cls.uniq_key]}, item, True)
        except Exception,e:
            logger.error('save error : %s'%(e))

    def remove(cls, item):
        try:
            cursor = cls.coll.remove({'_id':item['_id']})
        except Exception,e:
            logger.debug('remove error : %s'%(e))

    def dump(cls):
        try:
            cursor = cls.coll.find()
            for item in cursor:
                info = 'id ' + item['id'] + ' go_link ' + item['go_link'] + ' title ' + item['title'].strip()
                print info.encode('utf8')
        except Exception,e:
            logger.debug('dump error : %s'%(e))
            print('dump error : %s'%(e))
            return None
        return cursor

if __name__ == '__main__':
    t = Table('scrapy', 'test')
    t.dump()
