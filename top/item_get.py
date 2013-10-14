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
#    'fields':'iid,title,pic_url',
    'fields':'pic_url',
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
print ret_dict['item_get_response']["item"]["pic_url"].decode('string-escape')
string='http:\/\/img03.taobaocdn.com\/bao\/uploaded\/i3\/T1pzZjXdJbXXXhUys8_100755.jpg'
print string.decode('string-escape')
print string.replace('\/', '/')
op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')

params = {
    'method':'taobao.item.get',
#    'fields':'iid,title,pic_url',
#    'fields':'pic_url, list_time, dellist_time, web_only, with_hold_quantity, valid_thru, num',
    'fields' : 'item_img.position, item_img.url, pic_url',
#    'num_iid':'7765596787', 
#    'num_iid':'19042386019',
#    'num_iid':'19625170298',
    'num_iid' : '13696187103',
}
dict_str = op.get_result(params)
print dict_str


op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')

params = {
    'method':'taobao.item.get',
#    'fields':'iid,title,pic_url',
    'fields':'pic_url, cid',
#    'num_iid':'7765596787', 
    'num_iid':'21330584078',
}
dict_str = op.get_result(params)
print "start get cats"
print dict_str
print ret_dict['item_get_response']["item"]["pic_url"].decode('string-escape').replace('\/', '/')
op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')
params = {
    'method':'taobao.itemcats.get',
    'fields':'parent_cid, cid, name',
    'cids': '50023749',
}
dict_str = op.get_result(params)
print dict_str

def get_parent_cid(cid):
    op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')
    params = {
        'method':'taobao.itemcats.get',
        'fields':'parent_cid, cid, name',
        'cids': cid,
    }
    dict_str = op.get_result(params)
    print dict_str
    ret_dict = ast.literal_eval(dict_str)
    parent_cid = ret_dict['itemcats_get_response']['item_cats']['item_cat'][0]['parent_cid']
    if parent_cid == -1:
        name = 'null'
    else:
        name = ret_dict['itemcats_get_response']['item_cats']['item_cat'][0]['name']
    print 'parent_cid ' + str(parent_cid) + ' name ' + name
    return parent_cid

get_parent_cid('50023749')
def get_root_cid(cid):
    while True:
        if cid == 0:
            break
        parent_cid = get_parent_cid(cid)
        cid = parent_cid

get_root_cid('50023749')


def async_get_cats_info():
    op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')
    params = {
        'method':'taobao.topats.itemcats.get',
#        'output_format':'csv',
        'output_format':'json',
        'cids': '0',
        'type': '1',
    }
    dict_str = op.get_result(params)
    print dict_str
def async_get_result(taskid):
    op = OpenTaobao('12651461','80a15051c411f9ca52d664ebde46a9da')
    params = {
        'method':'taobao.topats.result.get',
        'task_id': taskid,
    }
    dict_str = op.get_result(params)
    print dict_str
    ret_dict = ast.literal_eval(dict_str)
    return ret_dict

async_get_cats_info()
#ret_dict = async_get_result(202689529)
ret_dict = async_get_result(202692393)
if ret_dict['topats_result_get_response']['task']['status'] == 'done':
    url = ret_dict['topats_result_get_response']['task']['download_url'].replace('\/', '/')
#{"topats_itemcats_get_response":{"task":{"created":"2013-10-12 17:37:19","task_id":202689529}}}
    form_data = urllib.urlencode({})
    buffer = urllib2.urlopen(url, form_data).read()
    file = open('download_cat.txt', 'w')
    file.write(buffer)
    file.close()
