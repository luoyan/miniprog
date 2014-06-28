netstat -anp|grep 'tcp '|grep ESTABLISHED|awk '{print $4;print $5}'|awk -F":" '{print $1}'|sort |uniq -c|sort -k 1 -nr
