ctags -R 
find . -name "*.py" -o -name "*.h" -o -name "*.cpp" -o -name "*.c" > cscope.files
cscope -bq
