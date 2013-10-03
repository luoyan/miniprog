# Create your views here.
#encoding=utf8

import sys
import os
import json
import urllib2
import datetime
import logging
import random
import hashlib
import logging
import traceback

from django.template import RequestContext
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from djangomako.shortcuts import render_to_response
from django.views.decorators.csrf import  ensure_csrf_cookie
logger = logging.getLogger(__name__)
def home(request):
    response = render_to_response("search.html", {}, context_instance=RequestContext(request))
    return response
