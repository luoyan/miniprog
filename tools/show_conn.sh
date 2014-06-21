host_ip=`hostname -i`
netstat -anp|grep tcp | grep $host_ip|awk '{print $5}'|awk -F":" '{print $1}'|uniq -c
