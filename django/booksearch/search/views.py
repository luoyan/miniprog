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
from booksearch import settings
from backends.gen_index import BookGenIndex
# Create your views here.
def home(request):
    response = render_to_response("index.html",{}, context_instance=RequestContext(request))
    return response

def index(request):
    t = get_template('booksearch2.html')
    book_items = []
    _conn = settings.mongoConn
    book_info = _conn['douban']['book_info']
    cursor = book_info.find()
    page_size = 8
    if request.GET.has_key('page'):
        page_num = int(request.GET['page'])
    else:
        page_num = 1
    count = 0
    end = True
    for item in cursor:
        count += 1
        if count > page_size * page_num:
            end = False
            break
        if count >= (page_size) * (page_num - 1) + 1:
            book_items.append(item)
    prev = page_num - 1
    next = page_num + 1
    c = template.Context({'book_items': book_items, 'end': end, 'page_num':page_num, 'prev':prev, 'next':next})
    html = t.render(c)
    return HttpResponse(html)

def images_files(request):
    path = request.get_full_path()
    file_name = path[len('/images/'):]
    file = open('/home/ubuntu/miniprog/django/booksearch/static/' + file_name)
    buffer = file.read()
    #return HttpResponse(request.get_full_path())
    return HttpResponse(buffer)
    #response = render_to_response("booksearch.html",{}, context_instance=RequestContext(request))
    #return response

def hello(request):
    response = HttpResponse("Hello world")
    return response

def current_datetime(request):
    now = datetime.datetime.now()
    html = "<html><body>It is now %s.</body></html>" % now
    return HttpResponse(html)

def render_template(request):
    raw_template = """<p>Dear {{ person_name }},</p>
    <p>Thanks for placing an order from {{ company }}. It's scheduled to
    ship on {{ ship_date|date:"F j, Y" }}.</p>

    {% if ordered_warranty %}
    <p>Your warranty information will be included in the packaging.</p>
    {% else %}
    <p>You didn't order a warranty, so you're on your own when
    the products inevitably stop working.</p>
    {% endif %}

    <p>Sincerely,<br />{{ company }}</p>"""
    t = Template(raw_template)
    import datetime
    c = Context({'person_name': 'John Smith',
        'company': 'Outdoor Equipment',
        'ship_date': datetime.date(2009, 4, 2),
        'ordered_warranty': False})
    html = t.render(c)
    return HttpResponse(html)

g_gi = BookGenIndex(True, True, False)
def search(request):
    if 'wd' in request.GET:
        wd = request.GET['wd']
        query = wd
        start = 0
        end = 10
        doc_list = g_gi.search(query, start, end)
        t = get_template('search_result.html')
        html = wd
        c = template.Context({'doc_list': doc_list})
        html = t.render(c)
        return HttpResponse(html)
    else:
        html = 'NONE'
    return HttpResponse(html)
