#! /usr/bin/env python
# -*- coding: utf-8 -*-
from scrapy.spider import BaseSpider
from scrapy.selector import HtmlXPathSelector
from scrapy.http import Request
from scrapy import log


debug = True

class DmozSpider(BaseSpider):
    name = "dmoz"
    allowed_domains = ["taobao.com", "ju.atpanel.com"]
    start_urls = [
    "http://top.taobao.com/"
    ]
    def get_index_by_attr(self, hxs, attr, name):
        i = 0
        list = []
        for h in hxs:
            id_list = h.select(attr).extract()
            if len(id_list) != 0:
                if (id_list[0] == name):
                    list.append(i)
            i = i + 1
        #self.log("list " + str(list) + " attr " + attr + " name " + name)
        if len(list) > 0:
            return list[0]
        else:
            return -1

    def get_node(self, hxs, attr_info_list):
        h = hxs
        for attr_info in attr_info_list:
            i = self.get_index_by_attr(h, attr_info[0], attr_info[1])
            if i < 0:
                return None
            h = h[i].select(attr_info[2])
        return h

    def parse_keywords(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-main', 'div'],
            ['@class', 'main-wrap', 'div'],
            ['@class', 'bangbox', 'div'],
            ['@class', 'bd', 'div'],
            ['@class', 'itemlist clearfix', 'div'],
            ['@class', 'items textlists', 'table'],
        ]
        h_table = self.get_node(hxs.select('//body/div'), attr_info_list)
        for keywords in h_table.select('tbody/tr/td/span/a/text()').extract():
            #self.log("keywords " + keywords)
            print keywords.encode("utf8")
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-main', 'div'],
            ['@class', 'main-wrap', 'div'],
            ['@class', 'bangbox', 'div'],
            ['@class', 'bd', 'div'],
            ['@class', 'itemlist clearfix', 'div'],
            ['@class', 'items textlists', 'div'],
            ['@class', 'pagination', 'div'],
            ['@class', 'page-bottom', 'a'],
            ['@class', 'page-next', '@href'],
        ]
        h_a = self.get_node(hxs.select('//body/div'), attr_info_list)
        if not h_a:
            self.log("no next page")
            return
        for url in h_a.extract():
            self.log("next page %s"%url)
            yield Request(url, callback=self.parse_keywords)

    def parse_rank(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-main', 'div'],
            ['@class', 'main-wrap', 'div'],
            ['@class', 'bangbox', 'div'],
            ['@class', 'bd', 'div'],
            ['@class', 'itemlist clearfix', 'div'],
            ['@class', 'text', 'table'],
        ]
        h_table = self.get_node(hxs.select('//body/div'), attr_info_list)
        for url in h_table.select('tfoot/tr/td/p/a/@href').extract():
            self.log("url " + str(url))
            yield Request(url, callback=self.parse_keywords)
            if debug:
                break            

    def parse_level3(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-main', 'div'],
            ['@class', 'main-wrap', 'div'],
            ['@id', 'categories', 'div'],
            ['@class', 'wrapper', 'dl']
        ]
        h_dl = self.get_node(hxs.select('//body/div'), attr_info_list)
        for url in h_dl.select('dd/a/@href').extract():
            self.log("url " + str(url))
            yield Request(url, callback=self.parse_rank)
            if debug:
                break            

    def parse_level2(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-main', 'div'],
            ['@class', 'main-wrap', 'div'],
            ['@id', 'categories', 'div'],
            ['@class', 'wrapper', 'dl']
        ]
        h_dl = self.get_node(hxs.select('//body/div'), attr_info_list)
        for url in h_dl.select('dd/a/@href').extract():
            self.log("url " + str(url))
        for url in h_dl.select('dd/a/@href').extract():
            yield Request(url, callback=self.parse_level3)
            if debug:
                break

    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-sub', 'div'],
            ['@id', 'nav', 'ul']
        ]
        h_ul = self.get_node(hxs.select('//body/div'), attr_info_list)
        for url in h_ul.select('li/a/@href').extract():
            self.log("url " + str(url))
        for url in h_ul.select('li/a/@href').extract():
            if url != 'http://top.taobao.com/index.php':
                yield Request(url, callback=self.parse_level2)
                if debug:
                    break
