#! /usr/bin/env python
# -*- coding: utf-8 -*-
class Base:
    def __init__(cls, b, c):
        cls.b = b
        cls.c = c
        cls.d = 0
    def print_info(a):
        print "b = " + str(a.b) + " c = " + str(a.c) + " d = " + str(a.d)
    def get_sum(cls):
        cls.d = cls.b + cls.c

def print_object(obj):
    for item in vars(obj).items():
        print "item " + str(item)
b1 = Base(1, 2)
b2 = Base(3, 4)
b1.print_info()
b2.print_info()
print_object(b1)
print_object(b2)
b3 = b2.get_sum()
#b3.print_info()
import datetime
from datetime import timedelta
print "time : " + str(datetime.datetime.now().date())
def handle_array(array):
    array.append(1)
    array.append(2)
a=[]
handle_array(a)
for item in a:
    print item
print "has attr " + str(hasattr(b1, 'b'))
#nick=u'麦苗科技001'
#nick=u'帽子女韩版'
#for i in xrange(len(nick)):
#    print "nick[" + str(i) + "] = " + nick[i]
#i = 0
#for c in nick:
#    i = i + 1
#    print "nick[" + str(i) + "] = " + c
#nick2=nick.encode('UTF16')
now = datetime.datetime.now()
next_month = now + timedelta(days=30)
delta = next_month.date() - now.date()
print now
print next_month
print now.date()
print next_month.date()
print delta
print delta.days
length = 4
for i in xrange(length):
    #print "[ " + str(i + 1) + " - " + str(length - i + 1) + " ]"
    for j in xrange(i + 1, length):
    #for j in xrange(length):
        print "i " + str(i) + " j " + str(j)
from sets import Set
s = Set()
s.add(1)
s.add(4)
def in_set(x, s):
    return x in s
print "1 in s "  + str(in_set(1, s))
print "4 in s "  + str(in_set(4, s))
print "10 in s "  + str(in_set(10, s))
list=[1, 2, 3, 4, 5, 6, 7]
compare_days = 5
for i in xrange(len(list) - 5 + 1):
    local_array = list[i:i+compare_days]
    print str(local_array)
#query_list=[u'\u535a\u4e16', u'bosch', u'bossanjerasu', u'\u535a\u65af\u4e16\u5bb6', u'bosssunwen', u'\u535a\u65af\u7ec5\u5a01', u'boulder', u'\u640f\u5c14\u5f97', u'bourbon', u'\u5e03\u5c14\u672c', u'bovis', u'\u535a\u7ef4\u65af', u'boyu', u'\u535a\u5b87', u'\u5e03\u6d1b\u514b', u'brogue', u'brother', u'\u5144\u5f1f', u'bruin', u'\u5b9d\u4e91', u'busen', u'\u6b65\u68ee', u'\u5e03\u7740\u5c4b', u'buzhewu', u'\u5361\u5229\u4e9a\u91cc', u'cagliari', u'cagliari exchange', u'camenae', u'\u5bb6\u7f8e\u4e50', u'capdase', u'\u5361\u767b\u4ed5', u'carbanni', u'\u5361\u90a6\u5c3c', u'cardeendino', u'\u5361\u4e39\u72c4\u8bfa', u'carose', u'\u5361\u8def\u4ed5', u'carrie', u'\u51ef\u8389', u'\u5361\u7279', u'carter', u'carters', u'cartinoe', u'\u5361\u63d0\u8bfa', u'cathleen', u'\u51ef\u601d\u7433', u'cefiro', u'\u585e\u98de\u6d1b', u'celestron', u'\u661f\u7279\u6717', u'cenda', u'\u529b\u6770', u'centrum', u'\u5584\u5b58\u7247', u'cezanne', u'\u5029\u4e3d', u'\u5de7\u5e1b', u'chaber', u'chanodug', u'\u590f\u8bfa\u591a\u5409', u'cherokee', u'\u5207\u8bfa\u57fa', u'\u5947\u7f8e', u'chimei', u'\u5947\u7f8e\u7684', u'chole', u'\u514b\u6d1b\u4f0a', u'chrisdien deny', u'\u514b\u96f7\u65af\u4e39\u5c3c', u'cinnamoroll', u'\u5927\u8033\u72d7', u'\u5a07\u97f5\u8bd7', u'clarins', u'\u5a07 \u97f5 \u8bd7', u'clash of the titans', u'\u4e4b\u8bf8\u795e\u6218', u'co co', u'\u53ef\u53ef', u'\u97e9\u4f0a', u'co.e', u'\u4f0a\u97e9', u'cocon', u'\u53ef\u5eb7', u'cold steel', u'\u51b7\u94a2', u'\u5eb7\u8d1d', u'combi', u'computer speaker', u'\u5587\u53ed \u7535\u8111', u'contax', u'\u5eb7\u6cf0\u65f6', u'\u9177\u8272', u'coolse', u'cottonfield', u'\u68c9\u7530', u'ctrip', u'\u643a\u7a0b', u'divella', u'\u6234\u7ef4\u5a1c', u'ladida', u'\u62c9\u8fea\u8fbe', u'ligeo', u'\u6b4c\u4e3d', u'macys', u'\u6885\u897f\u767e\u8d27', u'microphone', u'\u551b\u514b\u98ce', u'\u7f8e\u5ea6', u'mido', u'\u5ea6\u7f8e', u'novatec', u'\u4e45\u94b0', u'sanken', u'\u4e09\u80af', u'silran', u'\u4e1d\u5170', u'valention', u'\u534e\u4f26 \u5929\u5974', u'winnie the pooh', u'\u5c0f\u718a \u7ef4\u5c3c', u'cadidl', u'\u5361\u8fea\u9edb\u5c14', u'\u5361\u8fea\u6234\u5c14', u'lukas', u'\u5362\u5361\u65af', u'apple connector', u'\u5668\u82f9\u679c\u8fde\u63a5', u'\u5831\u865f', u'\u62a5\u53f7', u'baouna', u'\u5b9d\u6b27\u7eb3', u'bubchen', u'\u5b9d\u6bd4\u73ca', u'sudoku', u'\u6570\u72ec', u'cucu', u'\u5e93\u5e93', u'cup chair', u'\u6905\u9152\u676f', u'cuud', u'\u53e4\u897f', u'daiichi', u'\u5927\u4e00\u9488', u'danbi', u'\u4e39\u6bd4', u'\u5927\u663e', u'daxian', u'\u4ee3\u5353', u'da zzle', u'dazzle               ']
#for i in xrange(len(query_list)):
#query_list=[u'\u535a\u4e16', u'bosch']
#print query_list[0]
#for i in xrange(1):
#    print str(i)+" "+ query_list[i]
#    print query_list[i]
query=u'\u535a\u4e16'
print query.encode("utf8")
#for c in query:
#    print c
query2=query.encode('UTF-8')
#print "query2 " + query2
import ast
dict = ast.literal_eval("{'muffin' : 'lolz', 'foo' : 'kitty'}")
print str(dict)
#import mypackage
#import time
#time.sleep(60)
con_str=u'\u4e1d\u7eb7'
print con_str.encode("utf8")
dict={'a':1, 'b':2}
dict.update({'c':3})
print str(dict)

import decimal
print str(decimal.Decimal(str(round(345.2345, 2))))
