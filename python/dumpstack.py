import traceback
import sys

def produce_exception(recursion_level=2):
    sys.stdout.flush()
    if recursion_level:
        produce_exception(recursion_level-1)
    else:
        raise RuntimeError()

def call_function(f, recursion_level=2):
    if recursion_level:
        return call_function(f, recursion_level-1)
    else:
        return f()

def deco_add(func):
    return func;

def fun(self, n):
    return n * n;
class MyClass:
    @classmethod
    def fun2(cls, n):
        if n < 10:
            return cls.fun2(n+1)
        else :
            return add(n, 1);
@deco_add
@deco_add
@deco_add
@deco_add
def add(a, b):
    stack = traceback.extract_stack()
    print "type = " + stack.__class__.__name__
    print "len = " + str(len(stack))
    print '------------'
    for i in xrange(len(stack)):
        print "[" + str(i) + "]" + stack[i][2]
        print '------------'

    for i in xrange(len(stack)):
        print "[" + str(i) + "]" + str(stack[i])
    return a + b
from pprint import pprint
#print "res = " + str(add(1,2))
#print "res = " + str(fun(fun(fun(add(1,2)))))
c = MyClass()
print "res = " + str(MyClass.fun2(2))
#try:
#    produce_exception()
#except Exception, err:
#    print 'format_exception():'
#    exc_type, exc_value, exc_tb = sys.exc_info()
#    pprint(traceback.format_exception(exc_type, exc_value, exc_tb))
sStr1 = 'strchr'
sStr2 = 'strch'
print cmp(sStr1,sStr2)
print cmp(sStr1,'strchr')
class QueueName:
    SYB_AUTO_CAMPAIGN_OPTIMIZE='syb_auto_campaign_optimize'
    SYB_KEY_CAMPAIGN_OPTIMIZE='syb_key_campaign_optimize'
    SYB_AUTO_CREATIVE_OPTIMIZE='syb_auto_creative_optimize'


print QueueName.SYB_AUTO_CAMPAIGN_OPTIMIZE
import inspect
def __function__ ():
    caller = inspect.stack()[1]
    return caller[3]
def myfun2(a):
    print "fun : " + __name__
def myfun(a):
    print "fun : " +__function__()

def xxx_lock(xx_flag=0):
        sys.stdout.write("%s: START with %d\n" % (__name__, xx_flag))
xxx_lock()
myfun(1)
import socket
host = socket.gethostname()
print host
localIP = socket.gethostbyname(socket.gethostname())
print "local ip:%s "%localIP
ipList = socket.gethostbyname_ex(socket.gethostname())
for i in ipList:
        if i != localIP:
               print "external IP:%s"%i
#str1="helloworld"
#print 'find ' + str(str1.find('wor'))
#print 'find ' + str(str1.find('wr'))
#print 'index ' + str(str1.index('wor'))
#print 'index ' + str(str1.index('wr'))
import datetime
starttime = datetime.datetime.now()
endtime = datetime.datetime.now()
interval = endtime - starttime
import pdb
pdb.set_trace()
