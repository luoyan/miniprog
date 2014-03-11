import sys
import getopt
import pymongo
import datetime
import traceback

def usage(argv0):
    print >> sys.stderr, argv0 + " a b"
    print >> sys.stderr, "1>a+b 2>a-b"
def build_dict(filename):
    f = open(filename, 'r')
    d = {}
    while True:
        buffer = f.readline()
        if not buffer:
            break
        hash_code = hash(buffer)
        d[hash_code] = 0
    return d
def get_diff(file_name1, file_name2):
    f1 = open(file_name1, 'r')
    d = {}
    while True:
        buffer = f1.readline()
        if not buffer:
            break
        hash_code = hash(buffer)
        d[hash_code] = 0
        sys.stdout.write(buffer)
    f2 = open(file_name2, 'r')
    while True:
        buffer = f2.readline()
        if not buffer:
            break
        hash_code = hash(buffer)
        if not d.has_key(hash_code):
            sys.stderr.write(buffer)
            sys.stdout.write(buffer)

if __name__ == '__main__':
    if len(sys.argv) < 2:
        usage(sys.argv[0])
        sys.exit(-1)

    get_diff(sys.argv[2], sys.argv[1])
#    d2 = build_dict(sys.argv[2])
#    for key in d1:
#        if not d2.has_key(key):
#            sys.stderr.write(key)
#            sys.stdout.write(key)
#    for key in d2:
#        sys.stdout.write(key)

