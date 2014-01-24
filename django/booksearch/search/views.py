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
# Create your views here.
def home(request):
    response = render_to_response("index.html",{}, context_instance=RequestContext(request))
    return response

def index(request):
    #t = get_template('bookindex.html')
    t = get_template('booksearch2.html')
    #t = get_template('booksearch.html')
    #html = t.render(RequestContext(request))
    #return HttpResponse(html)
    item = {}
    item['author'] = u'叶萱'
    item['diguest'] = u'《你在我的左手边》内容简介：隐约，还是可以记起一张脸，那样白皙的皮肤、清秀的面容，目光里有深深的冷。伴随着那样鄙弃、不屑的声音，响彻我的记忆：“陶滢，你不漂...'
    item['img_url'] = 'http://img5.douban.com/mpic/s8460999.jpg'
    item['price'] = 22
    item['rating'] = 7.6
    item['title'] = u'你在我的左手边'
    item['url'] = 'http://book.douban.com/subject/3987275/'
    book_items = []
    book_items.append(item)
    c = template.Context({'book_items': book_items})
    html = t.render(c)
    return HttpResponse(html)
    #response = render_to_response("bookindex.html",{'book_items':book_items}, context_instance=RequestContext(request))
    #return response

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

def search(request):
    if 'wd' in request.GET:
        wd = request.GET['wd']
        html = wd
    else:
        html = 'NONE'
    return HttpResponse(html)
