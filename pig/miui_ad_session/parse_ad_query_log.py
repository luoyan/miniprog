#!/usr/bin/env python
#encoding: utf-8

import sys
import base64
import json

def parseLog():
    keysList = ["positions", "miui_ad_alg", "miui_ad_exp", "miui_ad_ads", "category_id", "position_type", "position", "package_name", "consumption_type", "download_no", "price", "consumption"]
    for buffer in sys.stdin:
        if not buffer:
            break
        line = buffer.strip()
        terms = line.split('\t')
        if len(terms) != 3:
            continue
        scribeInfo = terms[0]
        oldTime = terms[1]
        itemLevel2 = base64.b64decode(terms[2])
        if itemLevel2.startswith('{'):
            d = json.loads(itemLevel2)
            if d.has_key("log_event_type") and d['log_event_type'] == 'app_store_feature_expose' :
                clientInfo = d['client_info']
                algorithmInfo = d['algorithm_info']
                if clientInfo.has_key("imei"):
                    session = {}
                    session['imei'] = clientInfo["imei"]
                    session['Service'] = "AD"
                    session['Type'] = "algorithm_expose_detail"
                    session['Path'] = ""
                    for key in keysList:
                        if clientInfo.has_key(key):
                            session[key] = clientInfo[key]
                        elif algorithmInfo.has_key(key):
                            session[key] = algorithmInfo[key]

                    if algorithmInfo.has_key('algorithm_name'):
                        session['miui_ad_alg'] = algorithmInfo['algorithm_name']
                    if algorithmInfo.has_key('exp_id'):
                        session['miui_ad_exp'] = algorithmInfo['exp_id']

                    info = session['imei'] + '\t' + session['Service'] + '\t' + session['Type'] + '\t' + session['Path']
                    for key in keysList:
                        if session.has_key(key):
                            info = info + '\t' + session[key]
                        else:
                            info = info + '\t'
                    print info
if __name__ == '__main__':
    reload(sys)
    sys.setdefaultencoding('utf8')
    parseLog()
