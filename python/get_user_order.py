#! /usr/bin/env python
# -*- coding: utf-8 -*-
import os
import getopt
import datetime
import threading
import sys
import logging
import logging.config
from time import sleep
from datetime import  datetime
import datetime,time
import logging
import inspect
import ShopOptimize.conf.settings
import tao_models.vas_order_search
from tao_models.vas_order_search import VasOrderSearch

def is_vip_user(order_list):
    total_order_days = 0
    is_vip = 0
    is_new = 0
    for order in order_list:
        total_order_days += (order.order_cycle_end - order.order_cycle_start).days
        order_delta = datetime.datetime.now() - order.order_cycle_start
        if order_delta.days >= 0 and order_delta.days <= 15:
            is_new = 1
    if total_order_days >= 365:
        is_vip = 1

def get_order_info(nick , article_code):
    end = datetime.datetime.now()
    start = end - datetime.timedelta(days = 365*10)
    order_list = VasOrderSearch.search_vas_order_by_nick(article_code, start, end, nick)
    for order in order_list
        print "order.order_cycle_start " + str(order.order_cycle_start) + " order.order_cycle_end " + str(order.order_cycle_end)
    return is_vip_user(order_list)
get_order_info('一一3')
