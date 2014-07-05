if [ $# -ne 1 ] ; then
        echo "$0 hadoop/local"
        exit -1
fi
src_dir=src/main/python
input=/user/test/input
output=/user/test/output
mapper=wc_mapper.py
reducer=wc_reducer.py
if [ $1 == "hadoop" ] ; then
    hadoop fs -rmr $output
    stream_jar=$HADOOP_HOME/contrib/streaming/hadoop-streaming*.jar
    hadoop jar $stream_jar \
	-input $input \
	-output $output \
	-mapper $mapper \
    -file $src_dir/$mapper \
	-reducer $reducer \
    -file $src_dir/$reducer
else
    if ! test -d input ; then
        hadoop fs -get $input .
    fi
    cat input/* |$src_dir/wc_mapper.py |sort |$src_dir/wc_reducer.py
fi
