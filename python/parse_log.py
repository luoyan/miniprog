import re
f=open('access_log-20130818.log-20130818')
dict={}
while True:
    ln = f.readline()
    if not ln:
        break
    if 'ts-1796606&from' in ln:
        L=re.findall(r'(?<=from=)\w+',ln)
        if len(L) > 0:
            #print L[0]
            if not dict.has_key(L[0]):
                dict[L[0]] = 0
            dict[L[0]] = dict[L[0]] + 1

for key in dict:
    print "key = " + key + " count = " + str(dict[key])
