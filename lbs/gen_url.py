#! /usr/bin/env python
# -*- coding: utf-8 -*-
import sys
import os
import urllib
import urllib2
import json

def load_list(file_name):
	file = open(file_name)
	list = []
	while True:
		buffer = file.readline()
		if not buffer:
			break
		list.append(buffer.strip())
	return list

if __name__ == '__main__':
	list = load_list('place.list');
	city = '西安'
	encode_city = urllib.quote(city)
	ak = '2lsXelcqIziwWiEN6rmWSFbN'
	print 'title\taddress\tlongitude\tlatitude\tcoord_type'
	position_list = []
	for place in list:
		encode_place = urllib.quote(place)
		url = 'http://api.map.baidu.com/place/v2/search?&q=' + encode_place + "&region=" + encode_city + "&output=json&ak=" + ak
		#print "place " + place + " encode " + encode_place
		#print "place " + place + "url " + url
		url_ret = urllib.urlopen(url).read()
		#print "place " + place + " ret " + str(url_ret)
		url_ret_json = json.loads(url_ret);
		if url_ret_json['status'] == 0:
			for position_info in url_ret_json['results']:
				name = position_info['name']
				if not position_info.has_key('location'):
					continue
				lat = position_info['location']['lat']
				lng = position_info['location']['lng']
				address = position_info['address']
				coord_type = 1
				#print (name.encode('utf8') + "\t" + address.encode('utf8') + "\t" + str(lng) + "\t" + str(lat) + "\t" + str(coord_type))
				position = {
						'title' : name,
						'address' : address,
						'longitude' : lng,
						'latitude' : lat,
						'coord_type' : coord_type
				}
				position_list.append(position)
				print '{' 
				print '\"title\":\"' + name.encode('utf8') + "\","
				print '\"address\":\"' + address.encode('utf8')  + "\","
				print '\"longitude\":' + str(lng) + ","
				print '\"latitude\":' + str(lat) + ","
				print '\"coord_type\":' + str(lat)
				print "},"

#	print str(position_list)


