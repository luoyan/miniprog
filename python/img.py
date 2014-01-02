__author__ = 'gaonan'
#coding=utf-8
import Image
import sys

if __name__ == "__main__":
    infile = sys.argv[1]
    outfile = sys.argv[2]
    im = Image.open(infile)
    (x,y) = im.size #read image size
    #x_s = 100 #define standard width
    #y_s = y * x_s / x #calc height based on standard width
    y_s = 100 #define standard width
    x_s = x * y_s / y #calc height based on standard width
    out = im.resize((x_s,y_s),Image.ANTIALIAS) #resize image with high-quality
    out.save(outfile)
    print 'original size: ',x,y
    print 'adjust size: ',x_s,y_s
