function usage()
{
        local arg0=$0
        echo $arg0" groupId=com.luoyan.kafkatest artifactId=kafkatest"
}
if [ $# -ne 2 ] ; then
        usage
        exit -1
fi
groupId=$1
artifactId=$2
mvn archetype:generate \
          -DarchetypeGroupId=org.apache.maven.archetypes \
            -DgroupId=$1 \
              -DartifactId=$2
