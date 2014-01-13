# Scrapy settings for doubansearch project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/topics/settings.html
#

BOT_NAME = 'doubansearch'
BOT_VERSION = '1.0'

SPIDER_MODULES = ['doubansearch.spiders']
NEWSPIDER_MODULE = 'doubansearch.spiders'
USER_AGENT = '%s/%s' % (BOT_NAME, BOT_VERSION)
#WEBKIT_DOWNLOADER=['doubansearch']
#DOWNLOADER_MIDDLEWARES = {
#    'doubansearch.middleware.WebkitDownloader': 1,
#}
#import os
#os.environ["DISPLAY"] = ":3"

ITEM_PIPELINES=['doubansearch.pipelines.DoubansearchPipeline']
#mongodb set
MONGODB_SERVER = 'localhost'
MONGODB_PORT = 27017
MONGODB_DB = 'douban'
MONGODB_COLLECTION = 'book_info'
MONGODB_UNIQ_KEY = 'id'
MONGODB_ITEM_ID_FIELD = '_id'
MONGODB_SAFE = True
