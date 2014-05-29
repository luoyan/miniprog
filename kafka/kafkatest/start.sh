function usage()
{
        local arg0=$0
        echo $arg0" ProducerTest/ConsumerTest"
}
if [ $# -ne 1 ] ; then
        usage $0
        exit -1
fi
className=$1
java -cp ./target/kafkatest-1.0-SNAPSHOT-jar-with-dependencies.jar com.luoyan.kafkatest.$className

