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
# Create your views here.
def home(request):
    response = render_to_response("index.html",{}, context_instance=RequestContext(request))
    return response
