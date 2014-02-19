#! /usr/bin/env python
# -*- coding: utf-8 -*-
import datetime
import time
a='星期五, 31 七月 2009 09:30:21 +0800'
print str(datetime.datetime.now().strftime("%a, %d %b %Y %H:%M:%S +0800"))
a='2014-02-19 16:59:25'
print str(datetime.datetime.strptime(a, "%Y-%m-%d %H:%M:%S"))
