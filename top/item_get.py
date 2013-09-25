#! /usr/bin/env python
# -*- coding: utf-8 -*-
"""
@author: wulingping
@contact: wulingping@maimiaotech.com
@date: 2013-09-25 14:04
@version: 0.0.0
@license: Copyright Maimiaotech.com
@copyright: Copyright Maimiaotech.com

"""
import urllib
import urllib2
import time
import hashlib 
import md5 

class OpenTaobao:
    def __init__(self,app_key,sercet_code):
        self.app_key = app_key
        self.sercet_code = sercet_code
    def get_time(self):
        t = time.localtime()
        return time.strftime('%Y-%m-%d %X', t)
    def get_sign(self,params):
        params['format'] = 'json' 
        params.update({'app_key':self.app_key,'timestamp':self.get_time(),'v':'2.0'})
        src = self.sercet_code + ''.join(["%s%s" % (k, v) for k, v in sorted(params.iteritems())])
        return md5.new(src).hexdigest().upper()
    def get_result(self,params):
        params['sign'] = self.get_sign(params)
        form_data = urllib.urlencode(params)
        return urllib2.urlopen('http://gw.api.taobao.com/router/rest', form_data).read()


# the smallest taobao api python sdk
#
###############################################
#
# Usage:

op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')

params = {
    'method':'taobao.item.get',
    'fields':'iid,title,pic_url',
#    'num_iid':'7765596787', 
    'num_iid':'21330584078',
}

#print op.get_result(params)
dict_str = op.get_result(params)
print dict_str
op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')

params = {
    'method':'taobao.item.get',
    'fields':'iid,title,pic_url',
#    'num_iid':'7765596787', 
    'num_iid':'21330584078',
}
dict_str = op.get_result(params)
print dict_str
#a='hello'
#print hashlib.md5(a).hexdigest().upper()
#print md5.new(a).hexdigest().upper()
###############################################
#op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')
import ast
#dict_str=op.get_result(params)
#print dict_str
ret_dict = ast.literal_eval(dict_str)
print str(ret_dict)
print ret_dict['item_get_response']["item"]["pic_url"]
