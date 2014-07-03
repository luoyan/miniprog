function usage()
{
	local args0=$1
	echo $args0" check_kafka convert_input_list convert_output_dir check_input_list check_output_dir start_time end_time"
	echo $args0" check_kafka_staging"
	echo $args0" check_kafka_production_lg"
}
function start_mapreduce()
{
	hadoop jar mapred-0.0.1-SNAPSHOT-jar-with-dependencies.jar $@
}
function check_kafka()
{
	local convert_input_list=$1
	local convert_output_dir=$2
	local check_input_list=$3
	local check_output_dir=$4
	local start_time=$5
	local end_time=$6
    	echo "start to ConvertKafka ..."
    	start_mapreduce com.luoyan.mapred.ConvertKafka $convert_input_list $convert_output_dir
    	echo "end to ConvertKafka"
	echo "start to CheckKafka ..."
	start_mapreduce com.luoyan.mapred.CheckKafka $convert_output_dir:$check_input_list $check_output_dir $start_time $end_time
	echo "end to CheckKafka"
}
function check_kafka_staging()
{
	local convert_input_list=/user/kafka/input
	local convert_output_dir=/user/kafka/output
	#check_input_list=/user/check/input
	local check_input_list=/user/check/input.diff
	local check_output_dir=/user/check/output
	local start_time=1403798396590
	local end_time=1404271284740
	bash start_op.sh check_kafka $convert_input_list $convert_output_dir $check_input_list $check_output_dir $start_time $end_time
}

function check_kafka_production_lg()
{
	local date_prefix=/user/h_scribe/miui/miui_ad_store_service/year=2014/month=07/day=01
	local convert_input_list=$date_prefix/lg-hadoop-log01.bj/:$date_prefix/lg-miui-hd-log00.bj:$date_prefix/lg-miui-hd-log01.bj
	local convert_output_dir=/user/h_miui/luoyan/convert_kafka/output
	local check_prefix=/user/h_miui/luoyan/miui_ad_storm_app_store.log
	local check_input_list=${check_prefix}.20140630141922.0:${check_prefix}.20140630200312.0:${check_prefix}.20140701122130.0:${check_prefix}.20140701182552.0:${check_prefix}.20140701231557.0:${check_prefix}.20140701244819.0
	local check_output_dir=/user/h_miui/luoyan/check_kafka/output
	local start_time=`date -d "2014-07-01 12:00:00" "+%s"`
	local end_time=`date -d "2014-07-01 12:30:00" "+%s"`
	bash start_op.sh check_kafka $convert_input_list $convert_output_dir $check_input_list $check_output_dir $start_time $end_time
}

if [ $# -lt 1 ] ; then
	usage $0
	exit -1
fi
sub_command=$1
command=$0
#export PATH=/home/miuidata/luoyan/hadoop-2.0.0-mdh1.1-SNAPSHOT/bin/:$PATH
#which hadoop
#exit -1
echo "sub_command "$sub_command
if [ $sub_command == "check_kafka" ] ; then
	shift 1
	if [ $# -ne 6 ] ; then
		usage $command
		exit -1
	else
		check_kafka $@	
	fi
elif [ $sub_command == "check_kafka_staging" ] ; then
	check_kafka_staging
elif [ $sub_command == "check_kafka_production_lg" ] ; then
	check_kafka_production_lg
else
	usage $command
	exit -1
fi
