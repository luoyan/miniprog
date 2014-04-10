from redis import Redis
redisConn = Redis(host='redis1', port=6379, db=1)
#key='hello'
key=2
'''
value='world'
value={'1':'world'}
value=['1', 'world']
value={'1': {'2': 'world'}}
value={'1': {'2': ['hello', 'world']}}
redisConn.set(key, value)
'''
value2 = redisConn.get(key)
redisConn.save()
print 'value ' + str(value2)
