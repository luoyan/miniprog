#! /usr/bin/env python
# -*- coding: utf-8 -*-
from scrapy.spider import BaseSpider
from scrapy.selector import HtmlXPathSelector
from scrapy.http import Request
from scrapy import log
from scrapy.http import FormRequest
import json as simplejson
import httplib, urllib
import decimal
import re
import hashlib
from doubansearch.items import DoubansearchItem

def get_one(list):
    if len(list) > 0:
        return list[0]
    return None

def get_one_string(list):
    if len(list) > 0:
        return list[0]
    return ""

def get_num(string):
    list_str = re.findall('[0-9]*', string)
    for elem in list_str:
        if elem != "":
            return int(elem)
    return None
debug = False
class DoubanSearchSpider(BaseSpider):
    name = "doubansearch"
    allowed_domains = ["douban.com"]
    start_urls = [
    "http://www.douban.com/"
    ]

    def parse_tag(self, response):
        hxs = HtmlXPathSelector(response)
        #h_li_array = hxs.select('//body/div[@id="wrapper"]/div[@id="content"]/div[@class="grid-16-8 clearfix"]/div[@class="article"]/div[@class="subject-list"]/li[@class="subject-item"]')
        h_li_array = hxs.select('//body/div[@id="wrapper"]/div[@id="content"]/div[@class="grid-16-8 clearfix"]/div[@class="article"]/div[@id="subject_list"]/ul[@class="subject-list"]/li[@class="subject-item"]')
        #import pdb
        #pdb.set_trace()
        #file = open("b.html", 'w')
        #file.write(response._body)
        #file.close()
        print "h_li_array len " + str(len(h_li_array))
        ret_items = []
        for h_li in h_li_array:
            url = get_one(h_li.select('div[@class="pic"]/a[@class="nbg"]/@href').extract())
            img_url = get_one(h_li.select('div[@class="pic"]/a[@class="nbg"]/img/@src').extract())
            title = get_one(h_li.select('div[@class="info"]/h2/a/@title').extract())
            book_info = get_one(h_li.select('div[@class="info"]/div[@class="pub"]/text()').extract())
            book_info_array = book_info.split('/')
            author = book_info_array[0].strip()
            price_str = book_info_array[-1].strip()
            price = get_num(price_str)
            rating_str = get_one(h_li.select('div[@class="info"]/div[@class="star clearfix"]/span[@class="rating_nums"]/text()').extract())
            rating = float(rating_str)
            diguest = get_one_string(h_li.select('div[@class="info"]/p/text()').extract()).strip()
            prod = DoubansearchItem()
            print "url " + url.encode('utf8')
            print "img_url " + img_url.encode('utf8')
            print "title " + title.encode('utf8')
            print "author " + author.encode('utf8')
            print "price " + price_str.encode('utf8')
            print "rating " + rating_str.encode('utf8')
            print "diguest " + diguest.encode('utf8')
            prod['id'] = hashlib.md5(url).hexdigest().upper()
            prod['url'] = url
            prod['img_url'] = img_url
            prod['title'] = title
            prod['author'] = author
            prod['price'] = price
            prod['rating'] = rating
            prod['diguest'] = diguest
            ret_items.append(prod)
        next_url = get_one(hxs.select('//body/div[@id="wrapper"]/div[@id="content"]/div[@class="grid-16-8 clearfix"]/div[@class="article"]/div[@id="subject_list"]/div[@class="paginator"]/span[@class="next"]/a/@href').extract())
        if next_url:
            if next_url[0] == '/':
                next_url = u'http://book.douban.com' + next_url
            print 'next_url ' + next_url.encode('utf8')
            ret_items.append(Request(url = next_url, callback = self.parse_tag))
        return ret_items

    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        h_a_array = hxs.select('//body/div[@id="anony-book"]/div[@class="wrapper"]/div[@class="side"]/div[@class="mod"]/div[@class="book-cate-mod"]/div[@class="cate book-cate"]/ul/li/a')
        print "len " + str(len(h_a_array))
        for h_a in h_a_array:
            url = get_one(h_a.select('@href').extract())
            tag = get_one(h_a.select('text()').extract())
            if tag != u'更多':
                print "tag " + tag.encode('utf8') + " url " + url.encode('utf8')
            yield Request(url, callback = self.parse_tag)
            if debug :
                break
        #file = open("a.html", 'w')
        #file.write(response._body)
        #file.close()
