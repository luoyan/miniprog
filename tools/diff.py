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
        d[buffer] = 0
    return d

if __name__ == '__main__':
    if len(sys.argv) < 2:
        usage(sys.argv[0])
        sys.exit(-1)

    d1 = build_dict(sys.argv[1])
    d2 = build_dict(sys.argv[2])
    for key in d1:
        if not d2.has_key(key):
            sys.stderr.write(key)
            sys.stdout.write(key)
    for key in d2:
        sys.stdout.write(key)

