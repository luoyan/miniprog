#! /usr/bin/env python
# -*- coding: utf-8 -*-
import re
string=u'发布：09月23日 11:00'
list = re.findall('[0-9]*', string)
#print "list " + str(len(list))
for elem in list:
    if elem != "":
        print "elem " + str(elem)

