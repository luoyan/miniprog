CC = g++
CXX = g++
LINK = g++

CFLAGS = -fPIC -Wall -I. -I../utility
CXXFLAGS = $(CFLAGS)

ifeq ($(DEBUG), 1)
        CFLAGS += -g -DDEBUG
else
        CFLAGS += -O3 -DNDEBUG
endif

OBJS = Boss.o

.PHONY: all clean

all: $(OBJS)

clean:
	rm -Rf $(OBJS) $(patsubst %.o,%.lo,$(OBJS)) .libs
