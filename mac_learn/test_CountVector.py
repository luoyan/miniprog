#encoding=utf-8
import jieba
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.naive_bayes import BernoulliNB
from sklearn.feature_extraction import DictVectorizer
import numpy as np
from sklearn.naive_bayes import BernoulliNB

def get_vec_info(vec, measurements):
    print 'measurements ' + str(measurements)
    X = vec.fit_transform(measurements)
    print 'to_array ' + str(X.toarray())
    print 'feature_names ' + str(vec.get_feature_names())
    return X

def test_CountVector():
    #count_vect = CountVectorizer(min_df=1)
    count_vect = CountVectorizer()
    content = open('sk.txt').read()
    seg_list = jieba.cut(content)
    keyword_str_seg_list = ' '.join(seg_list)
    seg_list2 = keyword_str_seg_list.split(' ')
    seg_list3 = []
    for item in seg_list2:
        seg_list3.append(item.encode('utf8'))
#    import pdb
#    pdb.set_trace()
    #keyword_str_seg_list = content
    
    #x_train = count_vect.fit_transform(keyword_str_seg_list)
    #target_list = [0]
    #target = np.array(target_list)
    print str(seg_list3)
    corpus = [
            u'这是第一个文档',
            u'这是第二个第二个文档',
            u'第三个',
            u'是第一个文档吗'
            ]
    
    target_list = [0]
    sample_weight_list = [1]
    target = np.array(target_list)
    sample_weights = np.array(sample_weight_list)
    print 'target ' + str(target)
    print 'sample_weight ' + str(sample_weights)
    #get_vec_info(count_vect, corpus)

def test_CountVector2():
    corpus = [
        'This is the first document.',
        'This is the second second document.',
        'And the third one.',
        'Is this the first document?',
        ]
    vec = CountVectorizer()
    get_vec_info(vec, corpus)

def test_DictVector():
    measurements = [
        {'city': 'Dubai', 'temperature': 33.},
        {'city': 'London', 'temperature': 12.},
        {'city': 'San Fransisco', 'temperature': 18.},
        {'city': 'Hangzhou', 'temperature': 10., 'clean':50},
    ]
    vec = DictVectorizer()
    origin_X = get_vec_info(vec, measurements)
    X = np.array(origin_X.toarray())
    Y = [1, 3, 100, 5]
    #Y = [1, 1, 1, 1]
    Y = ['H', 'A', 'L', 'O']
    clf = BernoulliNB()
    clf.fit(X, Y)
    #['city=Dubai', 'city=Hangzhou', 'city=London', 'city=San Fransisco', 'clean', 'temperature']
    X_array = [
            [0, 1, 0, 0, 100, 50],
            [1, 0, 0, 0, 50, 10],
            ]
    for X_item in X_array:
        pred_ret = clf.predict(X_item)
        print 'X = ' + str(X_item) + ' pred_ret ' + str(pred_ret)

def test_BernouliNB():
    X = np.random.randint(2, size=(6, 100))
    print 'X ' + str(X)
    Y = np.array([1, 2, 3, 4, 4, 5])
    print 'Y ' + str(Y)
    clf = BernoulliNB()
    clf.fit(X, Y)
    for i in xrange(6):
        pred_ret = clf.predict(X[i])
        print 'X[' + str(i) + '] = ' + str(X[i]) + ' pred_ret ' + str(pred_ret)

def test_BernouliNB2():
    X = np.array([
        [0, 1],
        [1, 1],
        [1, 0],
        [-1, 1],
        [1000, 1000],
        [1000, 10001],
        [998, 800],
        [990, 1100],
        ]
            )
    print 'X ' + str(X)
    #Y = np.array([1, 1, 1, 1, 2, 2, 2, 2])
    Y = np.array([1, 2, 3, 4, 5, 6, 7, 8])
    print 'Y ' + str(Y)
    clf = BernoulliNB(alpha = 1)
    clf.fit(X, Y)
    X2 = np.array(
            [
            [1002, 1010],
            [1010, 910],
            [1003, 980],
            [1008, 1030],
            [-1, -1],
            [-3, -10],
            [40, 1],
            [1, -100],
            ]
            )
    for i in xrange(len(X2)):
        #pred_ret = clf.predict_proba(X2[i])
        pred_ret = clf.predict(X2[i])
        print 'X[' + str(i) + '] = ' + str(X[i]) + ' pred_ret ' + str(pred_ret)

def test_BernouliNB3():
    X = np.array([
        [1],
        [1],
        [1],
        [0],
        [0],
        [0],
        [0],
        [0],
        [0],
        [0],
        ]
            )
    print 'X ' + str(X)
    #Y = np.array([1, 1, 1, 1, 2, 2, 2, 2])
    Y = np.array([1, 1, 0, 1, 0, 0, 0, 0, 0, 0])
    print 'Y ' + str(Y)
    clf = BernoulliNB(alpha = 1)
    clf.fit(X, Y)
    X2 = np.array(
            [
            [1],
            [0],
            ]
            )
    for i in xrange(len(X2)):
        #pred_ret = clf.predict_proba(X2[i])
        pred_ret = clf.predict(X2[i])
        print 'X[' + str(i) + '] = ' + str(X2[i]) + ' pred_ret ' + str(pred_ret)

def test_BernouliNB4():
    X = np.array([
        [1, 1],
        [1, 1],
        [1, 1],
        [1, 0],
        [1, 0],
        [1, 0],
        [1, 0],
        [0, 0],
        [0, 0],
        [1, 0],
        ]
            )
    print 'X ' + str(X)
    #Y = np.array([1, 1, 1, 1, 2, 2, 2, 2])
    Y = np.array([1, 1, 0, 1, 0, 0, 0, 1, 1, 0])
    print 'Y ' + str(Y)
    clf = BernoulliNB(alpha = 1)
    clf.fit(X, Y)
    X2 = np.array(
            [
            [1, 1],
            ]
            )
    for i in xrange(len(X2)):
        #pred_ret = clf.predict_proba(X2[i])
        pred_ret = clf.predict(X2[i])
        print 'X[' + str(i) + '] = ' + str(X2[i]) + ' pred_ret ' + str(pred_ret)
if __name__ == '__main__':
    test_DictVector()
    #test_CountVector2()
    #test_CountVector()
    #test_BernouliNB()
    #test_BernouliNB2()
    #test_BernouliNB3()
