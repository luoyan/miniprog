#encoding=utf8

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
from jiapu import settings
curr_path = os.path.dirname(__file__)
sys.path.append(os.path.join(curr_path,'../'))
from comm_lib.table import Table
from comm_lib import utils
# Create your views here.
def home(request):
    t = get_template('index.html')
    table = Table('family', 'person')
    tree_node_dict = utils.build_tree(table)
    cursor = table.scan()
    person_list = []
    relationship_dict = {}
    for item in cursor:
        person_list.append(item)
        name = item['name']
        relationship_str = utils.get_relationship(table, tree_node_dict, u'罗琰', name)
        relationship_dict[item['name']] = relationship_str
        item['relationship'] = relationship_str
    c = template.Context({'person_list' : person_list, 'relationship_dict' : relationship_dict})
    #c = template.Context({})
    html = t.render(c)
    return HttpResponse(html)

def get_relationship(request):
    if request.GET.has_key('name'):
        name = request.GET['name']
        print 'name = ' + name
        table = Table('family', 'person')
        tree_node_dict = utils.build_tree(table)
        relationship_str = utils.get_relationship(table, tree_node_dict, u'罗琰', name)
        dest = name
        src = u'罗琰'
        html = dest + u'是' + src + u'的' + relationship_str
    else:
        html = ''
    return HttpResponse(html)
