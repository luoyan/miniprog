function usage() {
    local arg0=$1
    echo $arg0 "connect/dump/restore report_db/bidding_db/statistic_db/appmarket_db env=staging/production"
}
if [ $# -ne 3 ] ; then
    usage $0
    exit -1
fi
db_op=$1
db_type=$2
env=$3
if [ $db_type == "report_db" ] ; then
    if [ $env == "production" ] ; then
        user=adstat_wr
        passwd=a1336d3ffc6ab997fe591620f8071ca5
        host=10.101.30.42
        dbname=miui_ad_stat
    else
        user=root
        passwd=3487e498770b9740086144fc03140876
        host=zc-stage1-miui-ad02.bj
        dbname=miui_ad_stat_staging
    fi
elif [ $db_type == "bidding_db" ] ; then
    if [ $env == "production" ] ; then
        user=biddad_r
        passwd=biddad4325762
        host=192.168.1.41
        dbname=miui_bidding_ad
    else
        user=root
        passwd=3487e498770b9740086144fc03140876
        host=zc-stage1-miui-ad02.bj
        dbname=miui_bidding_ad_staging
    fi
elif [ $db_type == "statistic_db" ] ; then
    if [ $env == "production" ] ; then
        user=ad_statistic_wr
        passwd=OTI1ZmFhYmZlZWEyNDIzZjgxYzBmMDFl
        host=10.101.30.203
        dbname=miui_statistic
    else
        user=root
        passwd=3487e498770b9740086144fc03140876
        host=zc-stage1-miui-ad02.bj
        dbname=miui_statistic_staging
    fi
elif [ $db_type == "appmarket_db" ] ; then
    if [ $env == "production" ] ; then
        user=miui_app_r
        passwd=5726ebb6efe83e2d00d6c79dcc0a7f7b
        host=10.101.30.209
        dbname=miui_statistics_new
    #else
    #    user=root
    #    passwd=3487e498770b9740086144fc03140876
    #    host=zc-stage1-miui-ad02.bj
    #    dbname=miui_statistic_staging
    fi
else
    usage $0
    exit -1
fi

if [ $db_op == "connect" ] ; then
    mysql -h$host -u$user -p$passwd $dbname --default-character-set=utf8
elif [ $db_op == "dump" ] ; then
    mysqldump -h$host -u$user -p$passwd $dbname --skip-lock-tables >${db_type}.dump 
elif [ $db_op == "restore" ] ; then
    mysql -h$host -u$user -p$passwd $dbname < ${db_type}.dump 
else
    usage $0
    exit -1
fi
