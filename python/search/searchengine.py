#! /usr/bin/env python
# -*- coding: utf-8 -*-
import os
import getopt
import datetime
from time import sleep
import threading
import sys 
import random
import urllib2
import BeautifulSoup
from BeautifulSoup import BeautifulSoup
class Crawler:
    def __init__(self, dbname):
        pass

    def __del__(self):
        pass

    def dbcommit(self):
        pass

    def getentryid(self, table, field, value, createnew = True):
        return None

    def addtoindex(self, url, soup):
        print 'Indexing %s' % url

    def gettextonly(self, soup):
        return None

    def separatewords(self, text):
        return None

    def isindexed(self, url):
        return False

    def addlinkref(self, urlFrom, urlTo, linkText):
        pass

    def crawl(self, pages, depth=2):
	for i in range(depth):
            newpages = set()
            for page in pages:
                try:
                    print "open " + page
                    c = urllib2.urlopen(page)
                except:
                    print_exec()
                    exstr = traceback.format_exc()
                    print "can not download " + page + " error " + exstr
                    continue
                soup = BeautifulSoup(c.read())
                self.addtoindex(page, soup)
                links = soup('a')
                for link in links:
                    if ('href' in dict(link.attrs)):
                        url = urljoin(url, link['href'])
                        if url.find("'") == -1 :
                            continue
                        url = url.split("#")[0]
                        if url[0:4] == 'http' and not self.isindexed(url):
                            newpages.add(url)
                        linkText = self.gettextonly(link)
                        self.addlinkref(page, url, linkText)
                self.dbcommit()
            pages=newpages
#        pass

    def createindextables(self):
        pass

if __name__ == '__main__':
    page_list=['http://www.sina.com.cn']
    crawler = Crawler('linkdb')
    crawler.crawl(page_list, 2)
