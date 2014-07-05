#!/usr/bin/python
import sys
while True:
    buffer = sys.stdin.readline()
    if not buffer:
        break
    #print("map [" + buffer.strip() + "]")
    word_list = buffer.strip().split()
    for word in word_list:
        print word + " 1"
