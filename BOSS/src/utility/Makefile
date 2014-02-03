CC = g++
CXX = g++
LINK = g++

CFLAGS = -fPIC -Wall -I. -I../../include
CXXFLAGS = $(CFLAGS)

ifeq ($(DEBUG), 1)
	CFLAGS += -g -DDEBUG
else
	CFLAGS += -O3 -DNDEBUG
endif

OBJS = XConfig.o XMMap.o strnormalize.o Logger.o XString.o GlobalFunction.o

.PHONY: all clean

all: type $(OBJS)

type: 

clean:
	rm -Rf $(OBJS) $(patsubst %.o,%.lo,$(OBJS)) .libs 
