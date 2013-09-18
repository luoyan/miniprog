for i in range(3):
    try:
        print i
        break
    except:
        print "Unexpected exception!"
    finally:
        print "Finally"

def gen():
    for i in range(3):
        try:
            yield i
            print "yield " + str(i)
        except:
            print "Unexpected exception!"
        finally:
            print "Finally"

for i in gen():
    print i
    break
