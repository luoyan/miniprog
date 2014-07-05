#!/usr/bin/python
import sys
prev_word = None
total = 0
while True:
    buffer = sys.stdin.readline()
    if not buffer:
        break
    word_info = buffer.strip().split()
    word = word_info[0]
    count = int(word_info[1])
    if prev_word and word != prev_word:
        print prev_word + " " + str(total)
        total = 0
    total = total + count
    #print "total " + str(total) + " add count " + str(count)
    prev_word = word
if prev_word:
    print prev_word + " " + str(total)
