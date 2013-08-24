import time
from celery import Celery

CELERY_ANNOTATIONS = {
    'tasks.add': {'rate_limit': '10/m'}
}
#celery = Celery('tasks', broker='amqp://guest@localhost//')
celery = Celery('tasks', backend='amqp', broker='amqp://guest@127.0.0.1//')
@celery.task
def add(x, y):
	time.sleep(10)
	return x + y
