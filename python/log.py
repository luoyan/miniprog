import sys
import os
import unittest
import logging
logger = logging.getLogger('mylogger')
logger.setLevel(logging.DEBUG)
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
formatter = logging.Formatter('%(asctime)s %(levelname)-2s %(name)s.%(funcName)s:%(lineno)-5d %(message)s')
ch.setFormatter(formatter)
logger.addHandler(ch)
logger.info("message info")
logger.error("message error")
logger.warning("message warning")
logger.exception("message exception")
logger2 = logging.getLogger('mylogger2')
logger2.setLevel(logging.DEBUG)
logger2.info("message info")
args='helloworld'
code = 7
logger.info('exception: code %d *args:%s', code, str(args))
