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
logging.config.fileConfig('conf/consolelogger_jiapu.conf')
logger = logging.getLogger(__name__)
def usage(argv0):
    print argv0 + ' ' + ' save '
    print argv0 + ' ' + ' show_tree '
    print argv0 + ' ' + ' get_relationship '
    print argv0 + ' ' + ' get_person_info '
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
        dest = sys.argv[3].decode('utf8')
        src = sys.argv[2].decode('utf8')
        name_list = utils.get_relationship_name(tree_node_dict, src, dest)
        info = utils.name_list_to_info(name_list)
        print (dest + u'是' + src + u'的' + info).encode('utf8')
    elif sys.argv[1] == 'get_person_info':
        tree_node_dict = utils.build_tree(table)
        name = sys.argv[2].decode('utf8')
        item = utils.get_person_info(tree_node_dict, name)
        for sibling in item['siblings']:
            print ('sibling ' + sibling).encode('utf8')
    else:
        usage(sys.argv[0])
        sys.exit(-1)
    sys.exit(-1)
