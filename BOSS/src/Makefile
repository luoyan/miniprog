CC = g++
CXX = g++
LINK = g++
TOOL = 
CFLAGS	= -fPIC -Wall -O3 $(TOOL)

SUBDIRS = boss utility 

BIN_DIR = ../bin
INC_DIR	= ../include
LIB_DIR	= ../lib

INCL	= -I$(INC_DIR) 
	  
LIBS	= -L$(LIB_DIR) 

SO_FILE = libBoss2.so
	  
all: $(SUBDIRS) $(SO_FILE) 

$(SUBDIRS):
	$(MAKE) -C $@

libBoss2.so:
	$(CC) -shared -fPIC -o $(LIB_DIR)/$@ utility/Logger.o utility/strnormalize.o utility/XConfig.o utility/XString.o utility/GlobalFunction.o boss/Boss.o -ldl -L$(LIB_DIR)

clean: $(patsubst %,clean_%,$(SUBDIRS)) 
	rm -Rf $(LIB_DIR)/libBoss2.so $(LIB_DIR)/libBoss2.a

$(patsubst %,clean_%,$(SUBDIRS)):
	$(MAKE) -C $(patsubst clean_%,%,$@) clean

.PHONY: all clean $(SUBDIRS) $(patsubst %,clean_%,$(SUBDIRS))
