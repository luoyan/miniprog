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
    'fields':'iid,title,pic_url, post_fee, express_fee, ems_fee, freight_payer',
#    'num_iid':'7765596787', 
#    'num_iid':'21330584078',
    'num_iid':'18531954271',
}

#print op.get_result(params)
dict_str = op.get_result(params)
print dict_str
exit(0)
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
'''
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
'''
print 'start read cat_info2/99'
file = open('cat_info2/99', 'r')
#file = open('cat_info2/50019780', 'r')
#file = open('cat_info2/50006843', 'r')
g_dict_str = file.read()
import simplejson as json
g_ret_dict = json.loads(g_dict_str)
print g_ret_dict.keys()
def show_values(show_list, show_dict, level = 0):
    string = "" + 'level ' + str(level) + ' '
    for i in xrange(level):
        string = string + '    '
    for key in show_list:
        #print key + ' ' + show_dict[key].__class__.__name__
        if show_dict[key].__class__.__name__ == 'unicode':
            string2 = string + key + ' ' + show_dict[key].encode('utf8')
            print string2
        elif show_dict[key].__class__.__name__ == 'list':
            string2 = string + key + ' ' + str(len(show_dict[key]))
            print string2
            if len(show_dict[key]) > 0:
                #string2 = string + key + ' ' + str(show_dict[key][0].keys())
                #print string2
                for item in show_dict[key]:
                    if not item:
                        continue
                    show_values(item.keys(), item, level + 1)
        else:
            string2 = string + key + ' ' + str(show_dict[key])
            print string2
#show_list=['sortOrder', 'name', 'cid', 'parentCid', 'isParent']
show_list=['sortOrder', 'name', 'cid', 'categoryPropList', 'parentCid', 'isParent', 'featureList', 'childCategoryList']
#show_values(show_list, g_ret_dict)
print 'show_list.__class__.__name__ ' + show_list.__class__.__name__
def build_root_leave_dict(origin_cat_dict, root_leave_dict, level = 0):
    if not origin_cat_dict.has_key('cid'):
        return
    if not origin_cat_dict['cid']:
        return
    cid = origin_cat_dict['cid']
    if not root_leave_dict.has_key(cid):
        root_leave_dict[cid] = {}

    if not origin_cat_dict.has_key('childCategoryList'):
        return
    if not origin_cat_dict['childCategoryList']:
        return
    print 'origin_cat_dict.__class__.__name__ ' + origin_cat_dict.__class__.__name__ + " " + str(root_leave_dict)
    try:
        print 'level ' + str(level) + ' ' + str(len(origin_cat_dict['childCategoryList']))
        for item in origin_cat_dict['childCategoryList']:
            sub_cid = item['cid']
            root_leave_dict[cid][sub_cid] = {}
            build_root_leave_dict(item, root_leave_dict[cid], level + 1)
    except:
        return 

import os
def build_cat_tree(dir):
    files = os.listdir(dir)
    print 'files : ' + str(files)
    root_leave_dict = {}
    i = 0
    for file in files:
        #if file != '99':
        #    continue
        path=dir + file
        f = open(path, 'r')
        print 'open ' + path
        buffer = f.read()
        ret_dict = json.loads(buffer)
        #print 'ret_dict ' + str(ret_dict)
        build_root_leave_dict(ret_dict, root_leave_dict)
        i = i + 1
        #if i > 2:
        #    break

    print 'root_leave_dict ' + str(root_leave_dict)
    return root_leave_dict

def build_cat_map(root_leave_dict, root_cid, map_dict):

    for cid in root_leave_dict:
        if len(root_leave_dict[cid]) == 0:
            map_dict[cid] = root_cid
        else:
            build_cat_map(root_leave_dict[cid], root_cid, map_dict)

def build_cat_total_map(root_leave_dict):
    map_dict = {}
    for cid in root_leave_dict:
        build_cat_map(root_leave_dict[cid], cid, map_dict)

    return map_dict


root_dict = build_cat_tree('cat_info2/')

map_dict = build_cat_total_map(root_dict)
for key in map_dict:
    print str(key) + ' -> ' + str(map_dict[key])
