import time
from tasks import add
CELERY_ROUTES = {'tasks.add': {'queue': 'test'}}
a = []
for i in xrange(10):
	result = add.delay(4, 4)
	a.append(result)
	print "result [" + str(i) + "] = "+ str(result.ready())
#time.sleep(1)
#for i in xrange(10):
#	print "result [" + str(i) + "] = "+ str(a[i].ready())
#	if a[i].ready():
#		print "result [" + str(i) + "] = "+ str(a[i].get())
