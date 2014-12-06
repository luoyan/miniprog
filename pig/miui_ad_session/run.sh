pig="/home/miuidata/soft/pig-0.13.0/bin/pig"
export PATH=/opt/soft/jdk/bin:/usr/lib64/qt-3.3/bin:/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/sbin:/home/work/opshell/:/home/miuidata/bin:/home/miuidata/hadoop_Installs/hadoop-2.0.0-mdh1.0.0-SNAPSHOT/bin:/home/miuidata/soft/pig-0.13.0/bin
export JAVA_HOME=/opt/soft/jdk
command=$1
year=$2
month=$3
day=$4
#adQueryFile=/user/h_scribe/miui/miui_ad_query_service/year=$year/month=$month/day=$day/*
adQueryFile=/user/h_miui/luoyan/ad_statistic/miui_ad_query_service/year=$year/month=$month/day=$day/
#adStoreFile=/user/h_scribe/miui/miui_ad_store_service/year=$year/month=$month/day=$day/*
adStoreFile=/user/h_scribe/miui/miui_ad_store_service/year=$year/month=$month/day=$day/
#appStoreFile=/user/h_scribe/miliao_legacy/universal_log/year=$year/month=$month/day=$day/*
appStoreFile=/user/h_scribe/miliao_legacy/universal_log/year=$year/month=$month/day=$day/
sessionFile=/user/h_miui/luoyan/ad_statistic/session/year=$year/month=$month/day=$day/
statisticFile=/user/h_miui/luoyan/ad_statistic/statistic_test/year=$year/month=$month/day=$day/
pigFile=/user/h_miui/luoyan/ad_statistic/pig_test/year=$year/month=$month/day=$day/
${pig} -param AD_QUERY_LOG=${adQueryFile} -param AD_STORE_LOG=${adStoreFile} -param SESSION_LOG=${sessionFile} -param OUTPUT_DATA=${pigFile} miui_ad_session.pig
