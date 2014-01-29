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
# Create your views here.
def home(request):
    t = get_template('index.html')
    table = Table('family', 'person')
    cursor = table.scan()
    person_list = []
    for item in cursor:
        person_list.append(item)
    c = template.Context({'person_list': person_list})
    #c = template.Context({})
    html = t.render(c)
    return HttpResponse(html)

def get_relationship(request):
    if request.GET.has_key('name'):
        name = request.GET['name']
        print 'name = ' + name
        html = name
    else:
        html = ''
    return HttpResponse(html)
