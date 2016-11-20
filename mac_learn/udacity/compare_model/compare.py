# -*- coding:utf-8 -*-  
def train_classifier(clf, X_train, y_train):
    ''' 用训练集训练分类器 '''
    
    # 开始计时，训练分类器，然后停止计时
    start = time()
    clf.fit(X_train, y_train)
    end = time()
    
    # Print the results
    #print "Trained model in {:.4f} seconds".format(end - start)
    use_time = end - start
    return use_time

    
def predict_labels(clf, features, target):
    ''' 用训练好的分类器做预测并输出F1值'''
    
    # 开始计时，作出预测，然后停止计时
    start = time()
    y_pred = clf.predict(features)
    end = time()
    
    # 输出并返回结果
    #print "Made predictions in {:.4f} seconds.".format(end - start)
    use_time = end - start
    return f1_score(target.values, y_pred, pos_label='yes'), use_time


def train_predict(clf, X_train, y_train, X_test, y_test):
    ''' 用一个分类器训练和预测，并输出F1值 '''
    
    # 输出分类器名称和训练集大小
    #print "Training a {} using a training set size of {}. . .".format(clf.__class__.__name__, len(X_train))
    
    # 训练一个分类器
    train_use_time = train_classifier(clf, X_train, y_train)
    
    # 输出训练和测试的预测结果
    #print "F1 score for training set: {:.4f}.".format(predict_labels(clf, X_train, y_train))
    #print "F1 score for test set: {:.4f}.".format(predict_labels(clf, X_test, y_test))
    train_f1, train_predict_use_time = predict_labels(clf, X_train, y_train)
    test_f1, test_predict_use_time = predict_labels(clf, X_test, y_test)
    return clf.__class__.__name__, len(X_train), train_use_time, train_f1, train_predict_use_time, test_f1, test_predict_use_time
# 载入所需要的库
import numpy as np
import pandas as pd
from time import time
from sklearn.metrics import f1_score

# 载入学生数据集
student_data = pd.read_csv("student-data.csv")
#print "Student data read successfully!"
# 提取特征列
feature_cols = list(student_data.columns[:-1])

# 提取目标列 ‘passed’
target_col = student_data.columns[-1] 

# 显示列的列表
#print "Feature columns:\n{}".format(feature_cols)
#print "\nTarget column: {}".format(target_col)

# 将数据分割成特征数据和目标数据（即X_all 和 y_all）
X_all = student_data[feature_cols]
y_all = student_data[target_col]
def preprocess_features(X):
    ''' 预处理学生数据，将非数字的二元特征转化成二元值（0或1），将分类的变量转换成虚拟变量
    '''
    
    # 初始化一个用于输出的DataFrame
    output = pd.DataFrame(index = X.index)

    # 查看数据的每一个特征列
    for col, col_data in X.iteritems():
        
        # 如果数据是非数字类型，将所有的yes/no替换成1/0
        if col_data.dtype == object:
            col_data = col_data.replace(['yes', 'no'], [1, 0])

        # 如果数据类型是类别的（categorical），将它转换成虚拟变量
        if col_data.dtype == object:
            # 例子: 'school' => 'school_GP' and 'school_MS'
            col_data = pd.get_dummies(col_data, prefix = col)  
        
        # 收集转换后的列
        output = output.join(col_data)
    
    return output

X_all = preprocess_features(X_all)
#print "Processed feature columns ({} total features):\n{}".format(len(X_all.columns), list(X_all.columns))


# TODO：在这里导入你可能需要使用的另外的功能
from sklearn.cross_validation import ShuffleSplit
#X_all_sub = X_all.head(300)
# TODO：设置训练集的数量
num_train = 300

# TODO：设置测试集的数量
#num_test = X_all.shape[0] - num_train
num_test = 95

# TODO：把数据集混洗和分割成上面定义的训练集和测试集
X_train = None
X_test = None
y_train = None
y_test = None
cv = ShuffleSplit(n = X_all.shape[0], n_iter = 1, test_size = num_test, train_size = num_train, random_state = 0)

train_index = None
test_index = None
for train_index, test_index in cv:
    #print str(train_index) + " " + str(test_index)
    X_train = X_all.iloc[train_index]
    X_test = X_all.iloc[test_index]
    y_train = y_all.iloc[train_index]
    y_test = y_all.iloc[test_index]

# 显示分割的结果
#print "Training set has {} samples.".format(X_train.shape[0])
#print "Testing set has {} samples.".format(X_test.shape[0])

from sklearn import tree
from sklearn.svm import SVC
from sklearn.linear_model import LogisticRegression

# TODO：初始化三个模型
clf_A = tree.DecisionTreeClassifier()
clf_B = SVC()
clf_C = LogisticRegression()

# TODO：设置训练集大小
X_train_100 = 100
y_train_100 = 100

X_train_200 = 200
y_train_200 = 200

X_train_300 = 300
y_train_300 = 300

# TODO：对每一个分类器和每一个训练集大小运行'train_predict' 
# train_predict(clf, X_train, y_train, X_test, y_test)
clf_list = [clf_A, clf_B, clf_C]
print "clf_name\tX_train_len\ttrain_use_time\ttrain_f1\ttrain_predict_use_time\ttest_f1\ttest_predict_use_time"
for i in xrange(3):
    train_size = 100 * (i + 1)
    for j in xrange(3):
        clf_name, X_train_len, train_use_time, train_f1, train_predict_use_time, test_f1, test_predict_use_time = train_predict(clf_list[j], X_train[0:train_size], y_train[0:train_size], X_test, y_test)
        print clf_name + "\t" + str(X_train_len) + '\t' + str(train_use_time) + "\t" + str(train_f1) + "\t" + str(train_predict_use_time) + "\t"+ str(test_f1) + "\t" + str(test_predict_use_time)
