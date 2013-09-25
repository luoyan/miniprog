#coding=utf-8

from scrapy.item import Item, Field

class DoubansearchItem(Item):
    # define the fields for your item here like:
    id = Field() #id
    url = Field()
    img_url = Field()
    title = Field()
    author = Field()
    price = Field()
    rating = Field()
    diguest = Field()
