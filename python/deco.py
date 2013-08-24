#! /usr/bin/env python
# -*- coding: utf-8 -*-
def FirstDeco(func): 
    print '第一个装饰器' 
    return func


@FirstDeco 
def test(): 
    print 'asdf' 
    return 0

def SecondDeco(func):
    def _call():
        print "before SecondDeco " + func.__name__
        ret = func()
        print "after SecondDeco"
        return ret
    return _call

def ThirdDeco(func):
    def _call(*args, **kwargs):
        print "before ThirdDeco " + func.__name__ + "args = " + str(args) + "kwargs = " + str(kwargs)
        ret = func(*args, **kwargs)
        print "after ThirdDeco"
        return ret
    return _call

@SecondDeco 
def test2(): 
    print 'test2' 
    return 1

@SecondDeco 
def test3(): 
    print 'test3' 
    return 3

@SecondDeco 
def test4(): 
    print 'test4' 
    return 4

def test5(): 
    print 'test5' 
    return 5
@ThirdDeco
def test6(n): 
    print 'test6 ' + str(n)
    return 5


if __name__ == "__main__":
    print "ret = " + str(test())
    print '-----------------------------'
    print "ret = " + str(test2())
    print "-----------------------------"
    print "ret = " + str(test3())
    print "-----------------------------"
    print "ret = " + str(SecondDeco(test4)())
    print "-----------------------------"
    print "ret = " + str(SecondDeco(test5)())
    print "-----------------------------"
    print "ret = " + str(test6(6))
    print '-----------------------------'
