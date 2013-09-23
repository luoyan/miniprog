#! /usr/bin/env python
# -*- coding: utf-8 -*-
from scrapy.spider import BaseSpider
from scrapy.selector import HtmlXPathSelector
from scrapy.http import Request
from scrapy import log
from scrapy.http import FormRequest
import json as simplejson
import httplib, urllib
from doubansearch.items import DoubansearchItem

def get_one(list):
    if len(list) > 0:
        return list[0]
    return None

def get_one_string(list):
    if len(list) > 0:
        return list[0]
    return ""
debug = True
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
        for h_li in h_li_array:
            url = get_one(h_li.select('div[@class="pic"]/a[@class="nbg"]/@href').extract())
            img_url = get_one(h_li.select('div[@class="pic"]/a[@class="nbg"]/img/@src').extract())
            title = get_one(h_li.select('div[@class="info"]/h2/a/@title').extract())
            book_info = get_one(h_li.select('div[@class="info"]/div[@class="pub"]/text()').extract())
            book_info_array = book_info.split('/')
            author = book_info_array[0].strip()
            price_str = book_info_array[-1].strip()
            rating = get_one(h_li.select('div[@class="info"]/div[@class="star clearfix"]/span[@class="rating_nums"]/text()').extract())
            diguest = get_one_string(h_li.select('div[@class="info"]/p/text()').extract()).strip()
            print "url " + url
            print "img_url " + img_url
            print "title " + title
            print "author " + author
            print "price " + price_str
            print "rating " + rating
            print "diguest " + diguest 

    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        h_a_array = hxs.select('//body/div[@id="anony-book"]/div[@class="wrapper"]/div[@class="side"]/div[@class="mod"]/div[@class="book-cate-mod"]/div[@class="cate book-cate"]/ul/li/a')
        print "len " + str(len(h_a_array))
        for h_a in h_a_array:
            url = get_one(h_a.select('@href').extract())
            tag = get_one(h_a.select('text()').extract())
            if tag != u'更多':
                print "tag " + tag + " url " + url
            yield Request(url, callback = self.parse_tag)
            if debug :
                break
        #file = open("a.html", 'w')
        #file.write(response._body)
        #file.close()
