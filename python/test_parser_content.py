#coding=utf-8
__author__ = 'gaonan'
from readability.readability import Document
import urllib
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import re
import lxml.html.soupparser as soupparser
import lxml.etree as etree

def parser_content(url):
    rt_result = []
    dr = re.compile(r'<[^>]+>',re.S)
    html = urllib.urlopen(url).read()
    readable_article = Document(html).summary().encode('utf8')
    #print readable_article
    readable_article = readable_article.replace('&#13;','')
    cur_list = readable_article.split('\n')
    for item in cur_list:
        if '<img' in item and 'src=' in item:
            #print item.split('src=')[1].split('"')[1]
            dom = soupparser.fromstring(item)
            if len(dom) > 0:
                img_path = dom[0].xpath('.//img')
                for img in img_path:
                    rt_result.append(['0',img.get('src')])
        else:
            use_item = dr.sub('',item).replace(' ','')
            if len(use_item) > 10:
                rt_result.append(['1',use_item])
    return rt_result
if __name__ == '__main__':
    rt_result = parser_content(sys.argv[1])
    for item in rt_result:
        print item[0],'\t',item[1]
