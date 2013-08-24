from kombu import Queue, Exchange
BROKER_URL = 'amqp://guest:guest@app.maimiaotech.com:5672//'
CELERY_RESULT_BACKEND = 'amqp://guest:guest@app.maimiaotech.com:5672'
#    BROKER_URL = 'amqp://guest:guest@mm_246:5672//'
#    CELERY_RESULT_BACKEND = 'amqp://guest:guest@mm_246:5672'

CELERY_ROUTES = {'syb_test_wrapper': {'queue': 'syb_test'}
}
CELERY_ACKS_LATE = True
CELERYD_PREFETCH_MULTIPLIER=1
CELERY_TIMEZONE = 'Asia/Shanghai'
CELERY_ENABLE_UTC = True

CELERYD_LOG_FORMAT='%(asctime)s %(levelname)-2s %(name)s.%(funcName)s:%(lineno)-5d %(message)s'
CELERYD_TASK_LOG_FORMAT='%(asctime)s %(levelname)-2s %(name)s.%(funcName)s:%(lineno)-5d %(message)s %(task_name)s(%(task_id)s) %(message)s'

