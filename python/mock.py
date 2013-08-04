#from unittest.mock import MagicMock
from mock import MagicMock
class ProductionClass:
    def __init__(self):
        pass
    def method(self, a, b, c, key):
        print "key = " + key
        return a+b+c
    def func(self, a, b, c, key):
        print "return " + str(self.method(a, b, c, key))

thing = ProductionClass()
#thing.method = MagicMock(return_value=3)
#thing.method(3, 4, 5, key='value')
thing.func(3, 4, 5, key='value')
#thing.method.assert_called_with(3, 4, 5, key='value')
