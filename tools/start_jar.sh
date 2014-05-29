function usage()
{
        local arg0=$0
        echo $arg0" jar_file java_class"
}
if [ $# -ne 2 ] ; then
        usage $0
        exit -1
fi
jar_file=$1
java_class=$2
java -cp $jar_file $java_class
