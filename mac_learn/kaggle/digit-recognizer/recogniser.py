# -*- coding: utf-8 -*-
import numpy as np
import pandas as pd
import sys
from time import time
from sklearn.metrics import f1_score
from sklearn.metrics import accuracy_score
from sklearn.cross_validation import ShuffleSplit

def classify(features_train, labels_train, method):   
    ### import the sklearn module for GaussianNB
    ### create classifier
    ### fit the classifier on the training features and labels
    ### return the fit classifier
    
    
    ### your code goes here!
    clf = None
    start = time()
    if method == 'nb':
        from sklearn.naive_bayes import GaussianNB
        clf = GaussianNB()
        clf.fit(features_train, labels_train)
    elif method == 'lr':
        from sklearn.linear_model import LinearRegression
        clf = LinearRegression()
        clf.fit(features_train, labels_train)
    elif method == 'logicr':
        from sklearn.linear_model import LogisticRegression
        clf = LogisticRegression()
        clf.fit(features_train, labels_train)
    elif method == 'knn':
        from sklearn.neighbors import KNeighborsClassifier
        clf = KNeighborsClassifier()
        clf.fit(features_train, labels_train)
    elif method == 'dtc':
        from sklearn import tree
        clf = tree.DecisionTreeClassifier()
        clf.fit(features_train, labels_train)
    elif method == 'svm':
        from sklearn.svm import SVC
        clf = SVC()
        clf.fit(features_train, labels_train)
    elif method == 'mlp':
        from sklearn.neural_network import MLPClassifier
        clf = MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes=(5, 2), random_state=1)
        clf.fit(features_train, labels_train)
    end = time()
    return clf, end-start

def predict_labels(clf, features, target):
    ''' 用训练好的分类器做预测并输出F1值'''
    
    # 开始计时，作出预测，然后停止计时
    start = time()
    y_pred = clf.predict(features)
    end = time()
    
    # 输出并返回结果
    #print "Made predictions in {:.4f} seconds.".format(end - start)
    return f1_score(target.values, y_pred, pos_label='yes'), (end-start)

def train_and_get_score(method, X_train, y_train, X_test, y_test):
    clf, train_time = classify(X_train, y_train, method)   
    train_f1_score, predict_train_time = predict_labels(clf, X_train, y_train)
    test_f1_score, predict_test_time = predict_labels(clf, X_test, y_test)
    print "%s,train use %.4f seconds"%(method,train_time)
    print "%s,train f1 score %.4f, use %.4f seconds"%(method,train_f1_score, predict_train_time)
    print "%s,test f1 score %.4f, use %.4f seconds"%(method,test_f1_score, predict_test_time)

if len(sys.argv) < 3:
    print sys.argv[0] + " count=5000 method=nb,lr,logicr,knn,dtc,svm"
    sys.exit(-1)
count=int(sys.argv[1])
method=sys.argv[2]
start=time()
origin_data = pd.read_csv('train.csv')
end=time()
print "load train.csv %.4f seconds"%(end-start)
if count > 0:
    data = origin_data.head(count)
else:
    data = origin_data
labels = data['label']
features = data.drop('label',axis = 1)
print "train.csv has {} sample with {} variables.".format(*features.shape)
#print "features head [" + str(features.head()) + "]"
#print "labels head [" + str(labels.head()) + "]"
#import sys
#sys.exit(-1)
start=time()
#test_data = pd.read_csv('test.csv')
end=time()
#print "load test.csv %.4f seconds"%(end-start)
#print "test.csv has {} sample with {} variables.".format(*test_data.shape)
#cv_sets = ShuffleSplit(X.shape[0], n_iter = 10, test_size = 0.20, random_state = 0)
#cv = ShuffleSplit(n = X_all.shape[0], n_iter = 1, test_size = num_test, train_size = num_train, random_state = 0)
X_all=features
y_all=labels
X_train = None
X_test = None
y_train = None
y_test = None
cv = ShuffleSplit(n = X_all.shape[0], n_iter = 1, test_size = 0.20, random_state = 0)
for train_index, test_index in cv:
    X_train = X_all.iloc[train_index]
    X_test = X_all.iloc[test_index]
    y_train = y_all.iloc[train_index]
    y_test = y_all.iloc[test_index]
#nb,lr,logicr,knn,dtc,svm
train_and_get_score(method, X_train, y_train, X_test, y_test)
