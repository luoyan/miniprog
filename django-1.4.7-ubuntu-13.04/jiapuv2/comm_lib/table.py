#!/usr/bin/python
#encoding=utf8
__author__ = 'luoyan@maimiaotech.com'
import sys 
import os 
import logging 
import logging.config
logger = logging.getLogger(__name__)
from jiapuv2.settings import mongoConn

class Table(object):
    """
    class to operate cold_query  
    """
    _conn = mongoConn
    def __init__(self, db, coll, uniq_key = 'id'):
        self._db = db
        self._coll = coll
        self.uniq_key = uniq_key
        self.coll = self._conn[self._db][self._coll]

    def scan(cls, batch_size=30):
        try:
            cursor = cls.coll.find().batch_size(batch_size)
        except Exception,e:
            logger.debug('scan error : %s'%(e))
            return None
        return cursor

    def query(cls, name):
        try:
            cursor = cls.coll.find_one({'name': name})
        except Exception,e:
            logger.debug('query error : %s'%(e))
            return None
        return cursor

    def queryParents(cls, name):
        try:
            cursor = cls.query(name)
            if not cursor:
                return None, None
            father = None
            mother = None
            cursorScan = cls.scan()
            for item in cursorScan:
                if item.has_key('children'):
                    parentGender = None
                    parentName = None
                    parentCouple = None
                    if item.has_key('gender'):
                        parentGender = item['gender']
                    if item.has_key('name'):
                        parentName = item['name']
                    if item.has_key('couple'):
                        parentCouple = item['couple']
                    for child in item['children']:
                        if child == name:
                            if parentGender == 0:
                                father = parentName
                                if parentCouple:
                                    mother = parentCouple
                            elif parentGender == 1:
                                mother = parentName
                                if parentCouple:
                                    father = parentCouple
            return father, mother
        except Exception,e:
            logger.debug('query error : %s'%(e))
            return None, None

    def save(cls, item):
        try:
            cls.coll.update({cls.uniq_key:item[cls.uniq_key]}, item, True)
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
