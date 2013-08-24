import os,sys
import datetime
#import mock
from mock import MagicMock, Mock, patch, sentinel
#from mock import MagicMock
class SomeClass:
    def method(self):
        print "in method"
real = SomeClass()
real.method = mock.MagicMock(name='method')
real.method(3, 4, 5, key='value')
