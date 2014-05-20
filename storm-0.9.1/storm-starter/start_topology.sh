function usage()
{
        local argv0=$0
        echo $argv0" topologyClassName local/remote"
}
if [ $# -ne 2 ] ; then
        usage $0
        exit -1
fi
topologyClassName=$1
mode=$2
if [ $mode == "local" ] ; then
    topologyName=
else
    topologyName="test_"$topologyClassName
fi
#echo "./bin/storm jar ~/incubator-storm/examples/storm-starter/target/storm-starter-0.9.1-incubating-jar-with-dependencies.jar storm.starter.$topologyClassName $topologyName"
#./bin/storm jar ~/miniprog/storm-0.9.1/storm-starter/target/storm-starter-0.9.1-incubating-jar-with-dependencies.jar storm.starter.$topologyClassName $topologyName
./bin/storm jar ~/miniprog/storm-0.9.1/storm-starter/target/storm-starter-0.9.1-incubating.jar storm.starter.$topologyClassName $topologyName
