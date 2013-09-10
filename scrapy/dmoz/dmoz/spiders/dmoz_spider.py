from scrapy.spider import BaseSpider
from scrapy.selector import HtmlXPathSelector

def get_index_by_attr(hxs, attr, name):
    i = 0
    list = []
    for h in hxs:
        id_list = h.select(attr).extract()
        if len(id_list) != 0:
            if (id_list[0] == name):
                list.append(i)
        i = i + 1
    #print "list = " + str(list)
    return list[0]

def get_node(hxs, attr_info_list):
    h = hxs
    for attr_info in attr_info_list:
        i = get_index_by_attr(h, attr_info[0], attr_info[1])
        h = h[i].select(attr_info[2])
    return h
        
                          
class DmozSpider(BaseSpider):
    name = "dmoz"
    allowed_domains = ["taobao.com"]
    start_urls = [
    "http://top.taobao.com/"
    ]
    def parse(self, response):
        hxs = HtmlXPathSelector(response)
        attr_info_list = [
            ['@id', 'page', 'div'],
            ['@id', 'content', 'div'],
            ['@class', 'col-sub', 'div'],
            ['@id', 'nav', 'ul']
        ]
        h_ul = get_node(hxs.select('//body/div'), attr_info_list)
        for url in h_ul.select('li/a/@href').extract():
            print "url " + str(url)
        #filename = response.url.split("/")[-2]
        #open(filename, 'wb').write(response.body)
        
