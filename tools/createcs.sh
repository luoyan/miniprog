ctags -R 
find . -name "*.py" -o -name "*.h" -o -name "*.cpp" -o -name "*.c" -o -name "*.java" -o -name "*.scala" -o -name "*.cli" > cscope.files
cscope -bq
