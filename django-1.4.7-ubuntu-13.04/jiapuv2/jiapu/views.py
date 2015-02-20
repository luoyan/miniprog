#! /usr/bin/env python
# -*- coding: utf-8 -*-
import sys 
import MySQLdb
import sys
import os
import json 
import urllib2 
import datetime
import logging
import random
import hashlib
import hmac
import time

from django.template import RequestContext
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from djangomako.shortcuts import render_to_response
import datetime
from django.template import Template, Context
from django.template.loader import get_template
from django.template import Context
from django.http import HttpResponse
from django import template
curr_path = os.path.dirname(__file__)
sys.path.append(os.path.join(curr_path,'../'))
from comm_lib.table import Table
from comm_lib import utils
#from jiapuv2.settings import mongoConn
# Create your views here.
def search(request):
    table = Table('family', 'person')
    #tree_node_dict = utils.build_tree(table)
    person = request.GET.get('person', 'luoyan')
    cursor = table.query(person)
    father, mother = table.queryParents(person)

    family = {}
    if not cursor:
        return HttpResponse(json.dumps(family), content_type="application/json")

    family['name'] = cursor['name']
    if cursor.has_key('children'):
        family['children'] = cursor['children']
    if cursor.has_key('couple'):
        family['couple'] = cursor['couple']
    if cursor.has_key('gender'):
        family['gender'] = cursor['gender']
    if father:
        family['father'] = father
    if mother:
        family['mother'] = mother
    return HttpResponse(json.dumps(family), content_type="application/json")

def list(request):
    t = get_template('list.html')
    table = Table('family', 'person')
    tree_node_dict = utils.build_tree(table)
    cursor = table.scan()
    person_list = []
    relationship_dict = {}
    src = u'罗琰'
    for item in cursor:
        person_list.append(item)
        name = item['name']
        name_list = utils.get_relationship_name(tree_node_dict, src, name)
        info = utils.name_list_to_info(name_list)
        item['relationship'] = info
    c = template.Context({'person_list' : person_list, 'relationship_dict' : relationship_dict})
    html = t.render(c)
    return HttpResponse(html)
