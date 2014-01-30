#!/usr/bin/python
#encoding=utf8
import sys
import os
import datetime
curr_path = os.path.dirname(__file__)
sys.path.append(os.path.join(curr_path,'../'))
from comm_lib.table import Table
import logging
import logging.config
import traceback
from comm_lib import utils
def usage(argv0):
    print argv0 + ' ' + ' save '
    print argv0 + ' ' + ' show_tree '
    print argv0 + ' ' + ' get_relationship '
if __name__ == '__main__':
    if len(sys.argv) < 2:
        usage(sys.argv[0])
        sys.exit(-1)
    table = Table('family', 'person', 'name')
    if sys.argv[1] == 'save':
        utils.save(table)
    elif sys.argv[1] == 'show_tree':
        utils.show_tree(table)
    elif sys.argv[1] == 'get_relationship':
        tree_node_dict = utils.build_tree(table)
        info = utils.get_relationship(table, tree_node_dict, sys.argv[2].decode('utf8'), sys.argv[3].decode('utf8'))
        print (dest + u'是' + src + u'的' + info).encode('utf8')
    else:
        usage(sys.argv[0])
        sys.exit(-1)
    sys.exit(-1)
