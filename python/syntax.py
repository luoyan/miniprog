class Base:
    def __init__(cls, b, c):
        cls.b = b
        cls.c = c
    def print_info(a):
        print "b = " + str(a.b) + " c = " + str(a.c)

b1 = Base(1, 2)
b2 = Base(3, 4)
b1.print_info()
b2.print_info()
