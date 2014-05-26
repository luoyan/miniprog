ctags -R 
find . -name "*.py" -o -name "*.h" -o -name "*.cpp" -o -name "*.c" -o -name "*.java" > cscope.files
cscope -bq
