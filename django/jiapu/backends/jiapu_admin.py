#!/usr/bin/python
#encoding=utf8
import sys
import os
import datetime
curr_path = os.path.dirname(__file__)
sys.path.append(os.path.join(curr_path,'../'))
from comm_lib.table import Table
import logging
import logging.config
import traceback
#if __name__ == '__main__':
    #logging.config.fileConfig('../conf/consolelogger_jiapu.conf')
logging.config.fileConfig('conf/consolelogger_jiapu.conf')
logger = logging.getLogger(__name__)

items = []
items = [
        {
        'name' : u'罗连庆',
        'couple' : u'我奶奶unknown',
        'gender' : 'male',
        'children' : [u'罗微娟', u'罗信华', u'罗建华', u'罗国华', u'罗爱华', u'罗建平unkown', u'罗眯华unknown'],
        },
        {
        'name' : u'余冬强',
        'gender' : 'male',
        'couple' : u'叶亿仙',
        'children' : [u'余华飞', u'余建云', u'余建华', u'余建萍'],
        },
        {
        'name' : u'罗建华',
        'gender' : 'male',
        'couple' : u'余华飞',
        'children' : [u'罗琰'],
        },
        {
        'name' : u'罗琰',
        'gender' : 'male',
        },
        {
        'name' : u'余建云',
        'gender' : 'male',
        'couple' : u'沈迪珠',
        'children' : [u'余金金'],
        },
        {
        'name' : u'余建华',
        'gender' : 'female',
        'couple' : u'赵全平',
        'children' : [u'赵镭'],
        },
        {
        'name' : u'余建萍',
        'gender' : 'female',
        'couple' : u'钱松林',
        'children' : [u'钱余美'],
        },
        {
        'name' : u'罗信华',
        'gender' : 'male',
        'couple' : u'罗怡妈妈',
        'children' : [u'罗怡'],
        },
        {
        'name' : u'罗微娟',
        'gender' : 'female',
        'couple' : u'孙钏爸爸unknown',
        'children' : [u'孙钏'],
        },
        {
        'name' : u'罗国华',
        'gender' : 'female',
        'couple' : u'朱锋爸爸unknown',
        'children' : [u'朱锋'],
        },
        {
        'name' : u'罗爱华',
        'gender' : 'female',
        'couple' : u'王莎爸爸',
        'children' : [u'王莎'],
        },
        {
        'name' : u'罗建平unknown',
        'gender' : 'male',
        'couple' : u'三姨unknown',
        'children' : [u'罗琦'],
        },
        {
        'name' : u'罗眯华unknown',
        'gender' : 'male',
        'couple' : u'四姨unknown',
        'children' : [u'罗晨'],
        },
        {
        'name' : u'孙钏',
        'gender' : 'male',
        'couple' : u'孙芳妈妈unknown',
        'children' : [u'孙芳'],
        },
        {
        'name' : u'王莎',
        'gender' : 'female',
        'couple' : u'王莎丈夫unknown',
        'children' : [u'王莎女儿unknown'],
        },
        {
        'name' : u'余金金',
        'gender' : 'female',
        'couple' : u'余金金丈夫unknown',
        'children' : [u'余金金女儿unknown'],
        },
        ]
table = Table('family', 'person', 'name')
for item in items:
    table.save(item)
