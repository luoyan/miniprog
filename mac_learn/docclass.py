#! /usr/bin/env python
# -*- coding: utf-8 -*-
#encoding=utf8
import re
import math
import os

def getwords(doc):
    splitter = re.compile('\\W*')
    words = []
    for s in splitter.split(doc):
        #print 'split ' + s
        words.append(s)
    return words
'''
我们希望计算的概率是
一篇文档属于good的可能性。
P(Dg|Dx)
...
已知D0 D1 D2 ...是不是Dg
统计单词和是不是Dg的关系

P(W1)
P(Dg|W1)
P(Dg|W2)
直观上，可以统计出每个单词在每篇文章中出现的概率P(W1|D0)
每个单词在所有文章中出现的概率P(W1|D0+D1+...)
每个单词在所有good文章中出现的概率P(W1|Dg)
想知道一篇文章属于good的可能性，也就是P(Dg|W1W2W3 ... )
P(Dg|W1W2W3) = P(DgW1W2W3)/P(W1W2W3)
P(A|B) = P(AB)/P(B) = P(A1+A2|B) = P(A1|B) + P(A2|B) 
B已知，A不确定
P(B) = P(AB)/P(A|B)
P(AB) = P(A|B) * P(B) = P(BA) = P(B|A) * P(A)
A1和A2组成A，类似于good bad
贝叶斯公式
P(A|B1+B2) = P(A(B1+B2))/P(B1+B2) = (P(AB1) + P(AB2))/(P(B1) + P(B2))
= (P(A|B1) * P(B1) + P(A|B2) * P(B2)) / (P(B1 + B2))
= (P(B1|A) * P(A) + P(B2|A) * P(A)) / (P(B1 + B2))
= (P(B1|A) + P(B2|A)) * P(A) / (P(B1 + B2))
文档之间是或的关系
同一个文档的word之间是与的关系
P(D0) = P(W1W2W3) = P(W1)P(W2)P(W3)
本身P(W)出现得很低，P(D0)就更低了
P(Dg|Dx)不好算
P(Dx|Dg)
常识假象一
如果80%的垃圾邮件中出现won
P(W1|Db) = 80%
那么一封出现won的邮件是垃圾的可能性很大
P(Db|W1) = ?
P(W1|Db) = 80% won
P(W2|Db) = 90% Ha
P(W1W2|Db) = 72% won Ha
P(Db|W1) = P(W1|Db) * P(Db)/P(W1)
当P(Db)和P(W1)一定时，P(Db|W1)和P(W1|Db)是成正比的，与常识一致
当P(W1)很小，也就是won出现的概率很小，一旦出现won就是垃圾的可能性变得很大
只有当A B 相互独立时，P(AB) = P(A) * P(B)
常识假象二
如果80%的垃圾邮件中出现won
如果90%的垃圾邮件中出现Ha
那么同时出现Ha won的邮件为垃圾的可能性比只出现Ha或者won的要大得多
P(Db|W1W2) = P(W1W2|Db) * P(Db)/P(W1W2)
P(Dg|W1d0)
'''
class classifier:
    def __init__(self):
        self.doc_class = {}
        self.word_class = {}
    def load(self, dir_list):
        for dir in dir_list:
            type_name = dir
            files = os.listdir(dir)
            for file in files:
                self.doc_class[type_name + '/' + file ] = type_name

    def train_all(self):
        for file in self.doc_class:
            f = open(file, 'r')
            words = []
            while True:
                buffer = f.readline()
                if not buffer:
                    break
                array = buffer.strip().split()
                words.extend(array)

            class_name = self.doc_class[file]
            self.train(words, self.doc_class[file])

        for word in self.word_class:
            print '%s : %s'%(word, self.word_class[word])

    def train(self, words, class_name):
        for word in words:
            if not self.word_class.has_key(word):
                self.word_class[word] = {}
            if not self.word_class[word].has_key(class_name):
                self.word_class[word][class_name] = 0
            self.word_class[word][class_name] += 1
            #self.class_count[]

    def judge(self, dir):
        files = os.listdir(dir)
        for file in files:
            good = 0
            bad = 0
            file_name = dir + '/' + file
            f = open(file_name)
            while True:
                buffer = f.readline()
                if not buffer:
                    break
                words = buffer.strip().split()
                for word in words:
                    if not self.word_class.has_key(word):
                        continue
                    if self.word_class[word].has_key('good'):
                        good += self.word_class[word]['good']
                    if self.word_class[word].has_key('bad'):
                        bad += self.word_class[word]['bad']
            print '%s good %d bad %d'%(file_name, good, bad)
if __name__ == '__main__':
    doc=open('a.txt').read()
    getwords(doc)
    c = classifier()
    c.load(['good', 'bad'])
    c.train_all()
    c.judge('tojudge')
