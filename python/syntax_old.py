def fun1(condition):
    print "fun1 " + str(condition)
    return condition

def fun2(condition):
    print "fun2 " + str(condition)
    return condition
 
if not fun1(False) and not fun2(False):
    print "condition1"  
if not fun1(True) and not fun2(False):
    print "condition2"  

def fun_array_args(array):
    array.append('hello')

my_arr=[]
fun_array_args(my_arr)
for item in my_arr:
    print item
