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
logging.config.fileConfig('/home/ubuntu/miniprog/django/jiapu/conf/consolelogger_jiapu.conf')
logger = logging.getLogger(__name__)
# Create your views here.
def home(request):
    t = get_template('index.html')
    table = Table('family', 'person')
    tree_node_dict = utils.build_tree(table)
    cursor = table.scan()
    person_list = []
    relationship_dict = {}
    if request.session.has_key('login_name'):
        src = request.session['login_name']
    else:
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

def simple_home(request):
    t = get_template('simple_home.html')
    table = Table('family', 'person')
    tree_node_dict = utils.build_tree(table)
    if request.session.has_key('login_name'):
        src = request.session['login_name']
    else:
        src = u'罗琰'
    person_list = utils.get_all_person_info(tree_node_dict, src)
    c = template.Context({'person_list' : person_list})
    html = t.render(c)
    return HttpResponse(html)

def get_relationship(request):
    if request.session.has_key('login_name'):
        src = request.session['login_name']
    else:
        src = u'罗琰'
    if request.GET.has_key('name'):
        name = request.GET['name']
        table = Table('family', 'person')
        tree_node_dict = utils.build_tree(table)
        dest = name
        name_list = utils.get_relationship_name(tree_node_dict, src, dest)
        info = utils.name_list_to_info(name_list)
        html = dest + u'是' + src + u'的' + info
    else:
        html = ''
    return HttpResponse(html)

def get_person_info(request):
    if request.session.has_key('login_name'):
        src = request.session['login_name']
    else:
        src = u'罗琰'
    if request.GET.has_key('name'):
        name = request.GET['name']
        table = Table('family', 'person')
        tree_node_dict = utils.build_tree(table)
        person_info = utils.get_person_info(tree_node_dict, name, src)
        t = get_template('person.html')
        c = template.Context({'item' : person_info})
        html = t.render(c)
        return HttpResponse(html)
    else:
        html = ''
    return HttpResponse(html)

def login(request):
    if 'login_name' in request.GET and request.GET['login_name']:
        login_name = request.GET['login_name']
        print ('login ' + login_name).encode('utf8')
        request.session['login_name'] = login_name
        html = login_name
    else:
        html = ''
    #return HttpResponse(html)
    return HttpResponseRedirect('/')
