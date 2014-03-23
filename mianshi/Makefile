OBJS:=$(patsubst %.cpp, %, $(wildcard *.cpp))
all:$(OBJS)
$(OBJS):%:%.cpp
	g++ -g $< -o $@
clean:
	rm -rf $(OBJS)
