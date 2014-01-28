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
#if __name__ == '__main__':
    #logging.config.fileConfig('../conf/consolelogger_jiapu.conf')
logging.config.fileConfig('conf/consolelogger_jiapu.conf')
logger = logging.getLogger(__name__)
def save(table):
    items = [
            {
            'name' : u'罗连庆',
            'couple' : u'我奶奶unknown',
            'gender' : 0,
            'children' : [u'罗微娟', u'罗信华', u'罗建华', u'罗国华', u'罗爱华', u'罗建平unknown', u'罗眯华unknown'],
            },
            {
            'name' : u'余冬强',
            'gender' : 0,
            'couple' : u'叶亿仙',
            'children' : [u'余华飞', u'余建云', u'余建华', u'余建萍'],
            },
            {
            'name' : u'罗建华',
            'gender' : 0,
            'couple' : u'余华飞',
            'children' : [u'罗琰'],
            },
            {
            'name' : u'罗琰',
            'gender' : 0,
            },
            {
            'name' : u'罗琦',
            'gender' : 0,
            },
            {
            'name' : u'罗晨',
            'gender' : 0,
            },
            {
            'name' : u'罗怡',
            'gender' : 1,
            },
            {
            'name' : u'朱锋',
            'gender' : 0,
            },
            {
            'name' : u'赵镭',
            'gender' : 0,
            },
            {
            'name' : u'钱余美',
            'gender' : 1,
            },
            {
            'name' : u'卢思彤',
            'gender' : 1,
            },
            {
            'name' : u'孙芳',
            'gender' : 1,
            },
            {
            'name' : u'王莎女儿unknown',
            'gender' : 1,
            },
            {
            'name' : u'余建云',
            'gender' : 0,
            'couple' : u'沈迪珠',
            'children' : [u'余金金'],
            },
            {
            'name' : u'余建华',
            'gender' : 1,
            'couple' : u'赵全平',
            'children' : [u'赵镭'],
            },
            {
            'name' : u'余建萍',
            'gender' : 1,
            'couple' : u'钱松林',
            'children' : [u'钱余美'],
            },
            {
            'name' : u'罗信华',
            'gender' : 0,
            'couple' : u'罗怡妈妈',
            'children' : [u'罗怡'],
            },
            {
            'name' : u'罗微娟',
            'gender' : 1,
            'couple' : u'孙钏爸爸unknown',
            'children' : [u'孙钏'],
            },
            {
            'name' : u'罗国华',
            'gender' : 1,
            'couple' : u'朱锋爸爸unknown',
            'children' : [u'朱锋'],
            },
            {
            'name' : u'罗爱华',
            'gender' : 1,
            'couple' : u'王莎爸爸',
            'children' : [u'王莎'],
            },
            {
            'name' : u'罗建平unknown',
            'gender' : 0,
            'couple' : u'三姨unknown',
            'children' : [u'罗琦'],
            },
            {
            'name' : u'罗眯华unknown',
            'gender' : 0,
            'couple' : u'四姨unknown',
            'children' : [u'罗晨'],
            },
            {
            'name' : u'孙钏',
            'gender' : 0,
            'couple' : u'孙芳妈妈unknown',
            'children' : [u'孙芳'],
            },
            {
            'name' : u'王莎',
            'gender' : 1,
            'couple' : u'王莎丈夫unknown',
            'children' : [u'王莎女儿unknown'],
            },
            {
            'name' : u'余金金',
            'gender' : 1,
            'couple' : u'卢贤吉',
            'children' : [u'卢思彤'],
            },
            ]
    for item in items:
        table.save(item)

class TreeNode:
    def __init__(self, item):
        self.name = item['name']
        self.gender = item['gender']
        if item.has_key('couple'):
            self.couple = item['couple']
        else:
            self.couple = None
        if item.has_key('children'):
            self.children = item['children']
        else:
            self.children = []
        self.child_nodes = []
        self.siblings = []
        self.parents = [None, None]

def build_tree(table):
    cursor = table.scan()
    items = []
    for item in cursor:
        items.append(item)

    person_info = {}
    tree_node_dict = {}
    for item in items:
        person_info[item['name']] = item
        node = TreeNode(item)
        tree_node_dict[item['name']] = node
        if item.has_key('couple'):
            item2 = {}
            item2['name'] = item['couple']
            item2['gender'] = 1 - item['gender']
            item2['couple'] = item['name']
            item2['children'] = item['children']
            node2 = TreeNode(item2)
            tree_node_dict[item2['name']] = node2

    for name in tree_node_dict:
        node = tree_node_dict[name]
        if len(node.child_nodes) == 0:
            for child in node.children:
                if tree_node_dict.has_key(child):
                    node.child_nodes.append(tree_node_dict[child])
                    print ('name ' + node.name + ' gender ' + str(node.gender) + ' child append ' + child).encode('utf8')
                    if not tree_node_dict[child].parents[node.gender]:
                        tree_node_dict[child].parents[node.gender] = node
                    child_node = tree_node_dict[child]
                    if len(child_node.siblings) == 0:
                        for child in node.children:
                            if child != child_node.name:
                                if tree_node_dict.has_key(child):
                                    child_node.siblings.append(tree_node_dict[child])
                else:
                    print ('error no child info ' + child).encode('utf8')
    return tree_node_dict

def show_tree(table):
    tree_node_dict = build_tree(table)
    for name in tree_node_dict:
        node = tree_node_dict[name]
        print '--------------------------'
        print (u'姓名 ' + node.name).encode('utf8')
        if node.gender == 0:
            gender_str = u'男'
        else:
            gender_str = u'女'
        print (u'性别 ' + gender_str).encode('utf8')
        couple = node.couple
        if tree_node_dict.has_key(couple):
            if tree_node_dict[couple].gender == 0:
                couple_str = u'丈夫'
            else:
                couple_str = u'妻子'
            print (couple_str + ' ' + tree_node_dict[couple].name).encode('utf8')
        siblings_info = ''
        for i in xrange(len(node.siblings)):
            if i > 0:
                siblings_info = siblings_info + ', ' + node.siblings[i].name
            else:
                siblings_info = node.siblings[i].name
        if siblings_info:
            print (u'兄弟姐妹 ' + siblings_info).encode('utf8')
        children_info = ''
        for i in xrange(len(node.child_nodes)):
            if i > 0:
                children_info = children_info + ', ' + node.child_nodes[i].name
            else:
                children_info = node.child_nodes[i].name
        if children_info:
            print (u'子女 ' + children_info).encode('utf8')
        if node.parents[0]:
            print (u'父亲 ' + node.parents[0].name).encode('utf8')
        if node.parents[1]:
            print (u'母亲 ' + node.parents[1].name).encode('utf8')

def usage(argv0):
    print argv0 + ' ' + ' save '
    print argv0 + ' ' + ' show_tree '
if __name__ == '__main__':
    if len(sys.argv) < 2:
        usage(sys.argv[0])
        sys.exit(-1)
    table = Table('family', 'person', 'name')
    if sys.argv[1] == 'save':
        save(table)
    elif sys.argv[1] == 'show_tree':
        show_tree(table)
    sys.exit(-1)
