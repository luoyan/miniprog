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
#logging.config.fileConfig('conf/consolelogger_jiapu.conf')
logger = logging.getLogger(__name__)
def save(table):
    items = [
            {
            'name' : u'罗连庆',
            'couple' : u'我奶奶unknown',
            'gender' : 0,
            'children' : [u'罗微娟', u'罗新华', u'罗建华', u'罗国华', u'罗爱华', u'罗建平', u'罗建强'],
            'level' : 0,
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
            'couple' : u'何漪'
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
            'name' : u'鲁泽歆',
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
            'name' : u'罗新华',
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
            'name' : u'罗建平',
            'gender' : 0,
            'couple' : u'三姨unknown',
            'children' : [u'罗琦'],
            },
            {
            'name' : u'罗建强',
            'gender' : 0,
            'couple' : u'邵新华',
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
            'couple' : u'鲁杰冲',
            'children' : [u'鲁泽歆'],
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
        self.level = None
        self.siblings_index = None

def build_tree(table):
    cursor = table.scan()
    items = []
    for item in cursor:
        items.append(item)

    person_info = {}
    tree_node_dict = {}
    root_node = None
    for item in items:
        person_info[item['name']] = item
        node = TreeNode(item)
        tree_node_dict[item['name']] = node

        if item.has_key('couple'):
            item2 = {}
            item2['name'] = item['couple']
            item2['gender'] = 1 - item['gender']
            item2['couple'] = item['name']
            if item.has_key('children'):
                item2['children'] = item['children']
            node2 = TreeNode(item2)
            tree_node_dict[item2['name']] = node2
        if item.has_key('level'):
            root_node = node
        
        if item2.has_key('level'):
            root_node = node2


    for name in tree_node_dict:
        node = tree_node_dict[name]
        if len(node.child_nodes) == 0:
            for i in xrange(len(node.children)):
                child = node.children[i]
                if tree_node_dict.has_key(child):
                    node.child_nodes.append(tree_node_dict[child])
                    print ('name ' + node.name + ' gender ' + str(node.gender) + ' child append ' + child).encode('utf8')
                    if not tree_node_dict[child].parents[node.gender]:
                        tree_node_dict[child].parents[node.gender] = node
                    child_node = tree_node_dict[child]
                    child_node.siblings_index = i
                    if len(child_node.siblings) == 0:
                        for child in node.children:
                            if child != child_node.name:
                                if tree_node_dict.has_key(child):
                                    print ('append sibling ' + child).encode('utf8')
                                    child_node.siblings.append(tree_node_dict[child])
                else:
                    print ('error no child info ' + child).encode('utf8')
    root_node.level = 0
    get_level(root_node)
    return tree_node_dict

def get_level(node):
    level = node.level
    for i in xrange(2):
        if node.parents[i] and not node.parents[i].level:
            node.parents[i].level = level - 1
            get_level(node.parents[i])
    for i in xrange(len(node.siblings)):
        if not node.siblings[i].level:
            node.parents[i].level = level
            get_level(node.siblings[i])
    for i in xrange(len(node.child_nodes)):
        if not node.child_nodes[i].level:
            node.child_nodes[i].level = level + 1
            get_level(node.child_nodes[i])

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
        if node.level:
            print (u'辈份 ' + str(node.level)).encode('utf8')

def get_direct_paths(src_node, dest_node):
    if src_node.name == dest_node.name:
        return {'x':0, 'y':0}
    for i in xrange(2):
        if src_node.parents[i].name == dest_node.name:
            return {'x':0, 'y':1, 'dest_gender':dest_node.gender}
    for i in xrange(len(src_node.siblings)):
        if src_node.siblings[i].name == dest_node.name:
            if src_node.siblings[i].siblings_index and dest_node.siblings[i].siblings_index:
                if src_node.siblings[i].siblings_index < dest_node.siblings[i].siblings_index:
                    return {'x':1, 'y':0, 'dest_gender':dest_node.gender}
                else:
                    return {'x':-1, 'y':0, 'dest_gender':dest_node.gender}
    for i in xrange(len(src_node.child_nodes)):
        if src_node.child_nodes[i].name == dest_node.name:
            return {'x':0, 'y':-1, 'dest_gender':dest_node.gender}
    return None

relation_ship_map = [
            {
            'path' : [
                {'x':0, 'y':0},
                ],
            'name' : u'本人',
            },
            {
            'path' : [
                {'x':0, 'y':1, 'dest_gender':0},
                ],
            'name' : u'父亲',
            },
            {
            'path' : [
                {'x':0, 'y':1, 'dest_gender':1},
                ],
            'name' : u'母亲',
            },
            {
            'path' : [
                {'x':-1, 'y':0, 'dest_gender':0},
                ],
            'name' : u'哥哥',
            },
            {
            'path' : [
                {'x':-1, 'y':0, 'dest_gender':1},
                ],
            'name' : u'姐姐',
            },
            {
            'path' : [
                {'x':1, 'y':0, 'dest_gender':0},
                ],
            'name' : u'弟弟',
            },
            {
            'path' : [
                {'x':1, 'y':0, 'dest_gender':1},
                ],
            'name' : u'妹妹',
            },
        ]

g_relationship_define = [
        {
            'define' : u'本人',
            'level' : 0,
        },
        {
            'define' : u'妻子',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'丈夫',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'父亲',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'母亲',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'儿子',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'女儿',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'哥哥',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'姐姐',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'弟弟',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'妹妹',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'兄弟',
            'gender' : 0,
            'level' : 0,
        },
        {
            'define' : u'姐妹',
            'gender' : 1,
            'level' : 0,
        },
        {
            'define' : u'母亲的兄弟',
            'level' : 1,
            'name' : [u'舅舅', u'舅父']
        },
        {
            'define' : u'母亲的兄弟的妻子',
            'level' : 1,
            'name' : [u'舅妈', u'舅母']
        },
        {
            'define' : u'母亲的姐妹',
            'level' : 1,
            'name' : [u'阿姨', u'姨母']
        },
        {
            'define' : u'母亲的姐妹的丈夫',
            'level' : 1,
            'name' : [u'叔叔', u'姨父']
        },
        {
            'define' : u'父亲的哥哥',
            'level' : 1,
            'name' : [u'伯父']
        },
        {
            'define' : u'父亲的哥哥的妻子',
            'level' : 1,
            'name' : [u'伯母']
        },
        {
            'define' : u'父亲的弟弟',
            'level' : 1,
            'name' : [u'叔父']
        },
        {
            'define' : u'父亲的弟弟的妻子',
            'level' : 1,
            'name' : [u'婶母']
        },
        {
            'define' : u'父亲的姐妹',
            'level' : 1,
            'name' : [u'姑母']
        },
        {
            'define' : u'父亲的姐妹的丈夫',
            'level' : 1,
            'name' : [u'姑丈']
        },
        {
            'define' : u'父亲的父亲',
            'level' : 1,
            'name' : [u'祖父', u'爷爷']
        },
        {
            'define' : u'父亲的母亲',
            'level' : 1,
            'name' : [u'祖母', u'奶奶']
        },
        {
            'define' : u'母亲的父亲',
            'level' : 1,
            'name' : [u'外祖父', u'外公']
        },
        {
            'define' : u'母亲的母亲',
            'level' : 1,
            'name' : [u'外祖母', u'外婆']
        },
        {
            'define' : u'儿子的儿子',
            'level' : 1,
            'name' : [u'孙子']
        },
        {
            'define' : u'儿子的女儿',
            'level' : 1,
            'name' : [u'孙女']
        },
        {
            'define' : u'女儿的儿子',
            'level' : 1,
            'name' : [u'外孙']
        },
        {
            'define' : u'女儿的女儿',
            'level' : 1,
            'name' : [u'外孙女']
        },
        {
            'define' : u'姐妹的儿子',
            'level' : 1,
            'name' : [u'外甥']
        },
        {
            'define' : u'姐妹的女儿',
            'level' : 1,
            'name' : [u'外甥女']
        },
        {
            'define' : u'兄弟的儿子',
            'level' : 1,
            'name' : [u'侄子']
        },
        {
            'define' : u'兄弟的女儿',
            'level' : 1,
            'name' : [u'侄女']
        },
        {
            'define' : u'父亲的兄弟的儿子',
            'level' : 1,
            'name' : [u'堂兄弟']
        },
        {
            'define' : u'父亲的兄弟的女儿',
            'level' : 1,
            'name' : [u'堂姐妹']
        },
        {
            'define' : u'父亲的姐妹的儿子',
            'level' : 1,
            'name' : [u'表兄弟']
        },
        {
            'define' : u'父亲的姐妹的女儿',
            'level' : 1,
            'name' : [u'表姐妹']
        },
        {
            'define' : u'母亲的兄弟的儿子',
            'level' : 1,
            'name' : [u'表兄弟']
        },
        {
            'define' : u'母亲的兄弟的女儿',
            'level' : 1,
            'name' : [u'表姐妹']
        },
        {
            'define' : u'母亲的姐妹的儿子',
            'level' : 1,
            'name' : [u'表兄弟']
        },
        {
            'define' : u'母亲的姐妹的女儿',
            'level' : 1,
            'name' : [u'表姐妹']
        },
        {
            'define' : u'母亲的表兄弟',
            'level' : 2,
            'name' : [u'表舅']
        },
        {
            'define' : u'表姐妹的女儿',
            'level' : 2,
            'name' : [u'表侄女']
        },
        {
            'define' : u'表姐妹的儿子',
            'level' : 2,
            'name' : [u'表侄子']
        },
        {
            'define' : u'表姐妹的丈夫',
            'level' : 2,
            'name' : [u'表姐夫', u'表妹夫']
        },
        {
            'define' : u'堂姐妹的丈夫',
            'level' : 2,
            'name' : [u'堂姐夫', u'堂妹夫']
        },
        {
            'define' : u'堂兄弟的妻子',
            'level' : 2,
            'name' : [u'堂嫂', u'堂弟妹']
        },
        {
            'define' : u'表兄弟的妻子',
            'level' : 2,
            'name' : [u'表嫂', u'表弟妹']
        },
        ]
def get_relationship_next_node_list(tree_node_dict, atom_define, gender, node_list):
    next_node_list = []
    for node in node_list:
        if atom_define == u'本人':
            next_node_list.append(node)
        elif atom_define == u'妻子' or atom_define == u'丈夫':
            if node.couple :
                next_node = tree_node_dict[node.couple]
                if next_node.gender != gender:
                    continue
                next_node_list.append(next_node)
            else:
                continue
        elif atom_define == u'父亲' or atom_define == u'母亲':
            if node.parents[gender] :
                next_node = tree_node_dict[node.parents[gender].name]
                if next_node.gender != gender:
                    continue
                next_node_list.append(next_node)
            else:
                continue
        elif atom_define == u'儿子' or atom_define == u'女儿':
            for child_node in node.child_nodes:
                if child_node.gender != gender:
                    continue
                next_node_list.append(child_node)
        elif atom_define == u'哥哥' or atom_define == u'姐姐':
            for next_node in node.siblings:
                if next_node.siblings_index >= node.siblings_index or next_node.gender != gender:
                    continue
                next_node_list.append(next_node)
        elif atom_define == u'弟弟' or atom_define == u'妹妹':
            for next_node in node.siblings:
                if next_node.siblings_index <= node.siblings_index or next_node.gender != gender:
                    continue
                next_node_list.append(next_node)
        elif atom_define == u'兄弟' or atom_define == u'姐妹':
            for next_node in node.siblings:
                if next_node.gender != gender:
                    continue
                next_node_list.append(next_node)
    return next_node_list

def get_relationship_node_list_by_define(tree_node_dict, name, define, gender, src_node_list, atom_define_dict):
    atom_define_array = define.split(u'的')
    node_list = src_node_list
    next_node_list = []
    for atom_define in atom_define_array:
        if not atom_define_dict.has_key(atom_define):
            gender = None
        else:
            gender = atom_define_dict[atom_define]
        next_node_list = get_relationship_next_node_list(tree_node_dict, atom_define, gender, node_list)
        node_list = next_node_list

    return next_node_list

def prehandle_relationship_define(relationship_define):
    name_to_define_dict = {}
    for item in relationship_define:
        define = item['define']
        if not item.has_key('name'):
            name = [define]
        else:
            name = item['name']
        for sub_name in name:
            if not name_to_define_dict.has_key(sub_name):
                name_to_define_dict[sub_name] = []
            name_to_define_dict[sub_name].append(item)

    new_relationship_define = []
    for item in relationship_define:
        if item['level'] < 2:
            new_relationship_define.append(item)
        elif item['level'] == 2:
            define = item['define']
            name = item['define']
            atom_define_array = define.split(u'的')
            for atom_define in atom_define_array:
                if not name_to_define_dict.has_key(atom_define):
                    print ('unknown define ' + define).encode('utf8')
                    return
                if name_to_define_dict[atom_define][0]['level'] == 1:
                    for item2 in name_to_define_dict[atom_define]:
                        new_define = define.replace(atom_define, item2['define'])
                        item3 = {'define' : new_define, 'level' : 1, 'name': item['name']}
                        new_relationship_define.append(item3)
    for item in new_relationship_define:
        print ('define ' + item['define'] + ' level ' + str(item['level'])).encode('utf8')
        if item.has_key('name'):
            for name in item['name']:
                print ('name ' + name).encode('utf8')
    return new_relationship_define

def get_relationship_name(tree_node_dict, src, dest):
    src_node = tree_node_dict[src]
    node = src_node
    dest_node = tree_node_dict[dest]
    atom_define_dict = {}
    relationship_define = prehandle_relationship_define(g_relationship_define)
    for item in relationship_define:
        if item.has_key('gender'):
            gender = item['gender']
            define = item['define']
            atom_define_dict[define] = gender
    
    for item in relationship_define:
        define = item['define']
        if item.has_key('name'):
            name = item['name']
        else:
            name = [define]
        if item.has_key('gender'):
            gender = item['gender']
        else:
            gender = None
        dest_node_list = get_relationship_node_list_by_define(tree_node_dict, name, define, gender, [src_node], atom_define_dict)
        for node in dest_node_list:
            if node.name == dest_node.name:
                return name
    return None

def get_all_person_info(tree_node_dict, src):
    item_list = []
    for name in tree_node_dict:
        relationship_name_list = get_relationship_name(tree_node_dict, src, name)
        info = name_list_to_info(relationship_name_list)
        item = {}
        item['name'] = name
        item['relationship'] = info
        item_list.append(item)
    return sorted(item_list, key = lambda x:x['name'])

def same_path(path1, path2):
    if len(path1) != len(path2):
        return False
    for i in xrange(len(path1)):
        if path1[i]['x'] != path2[i]['x']:
            return False
        if path1[i]['y'] != path2[i]['y']:
            return False
        if path1[i].has_key('dest_gender') != path2[i].has_key('dest_gender'):
            return False
        if path1[i].has_key('dest_gender') and path1[i]['dest_gender'] != path2[i]['dest_gender']:
            return False
    return True

def get_shortest_path(tree_node_dict, src, dest):
    if not tree_node_dict.has_key(src):
        return None
    if not tree_node_dict.has_key(dest):
        return None
    src_node = tree_node_dict[src]
    dest_node = tree_node_dict[dest]
    path = get_direct_paths(src_node, dest_node)
    if not path:
        return None
    path = [path]
    for item in relation_ship_map:
        if same_path(path, item['path']):
            return item['name']
    return None

def get_relationship(table, tree_node_dict, src, dest):
    name = get_shortest_paths(tree_node_dict, src, dest)
    if not name:
        return u'未知关系'
    else:
        return name

def name_list_to_info(name_list):
    if not name_list:
        info = u'未知关系'
    else:
        info = ''
        for i in xrange(len(name_list)):
            if i == 0:
                info = name_list[i]
            else:
                info = info + u'或' + name_list[i]
    return info

def get_relationship_dict(tree_node_dict, name, src):
    d = {}
    d['name'] = name
    name_list = get_relationship_name(tree_node_dict, src, name)
    info = name_list_to_info(name_list)
    d['relationship'] = info
    return d

def get_person_info(tree_node_dict, name, src):
    item = {}
    if not tree_node_dict.has_key(name):
        return item
    item['name'] = get_relationship_dict(tree_node_dict, name, src)
    node = tree_node_dict[name]
    if node.parents[0]:
        item['father'] = get_relationship_dict(tree_node_dict, node.parents[0].name, src)
    if node.parents[1]:
        item['mother'] = get_relationship_dict(tree_node_dict, node.parents[1].name, src)
    item['siblings'] = []
    for sibling in node.siblings:
        d = get_relationship_dict(tree_node_dict, sibling.name, src)
        item['siblings'].append(d)
    item['children'] = []
    for child in node.child_nodes:
        d = get_relationship_dict(tree_node_dict, child.name, src)
        item['children'].append(d)
    if node.couple:
        item['couple'] = get_relationship_dict(tree_node_dict, node.couple, src)
    return item
