#!/bin/bash
cat>ssh_list <<EOF
#name user ip passwd
debug_mm253 debug 223.5.20.253 mysmngzd_253
root_mm253 root 223.5.20.253 hksjlscq_253
ops_taoci ops taoci.maimiaotech.com wmsyjr_244
ops_xcw ops xcw.maimiaotech.com wmsyjr_242
debug_syb debug syb.maimiaotech.com wmshc_243
ops_mm245 ops 223.5.20.245 wmsyjr_wy2
debug_mm245 debug 223.5.20.245 chrdw_245
prd_2C prd2c 121.52.232.219 ztmhsprd
ly_2C luoyan 121.52.232.219 62717038
ly_dev luoy app.maimiaotech.com 62717038
EOF
usage()
{
    local argv0=$0
    echo "$0 ls"
    grep -v "^#" ssh_list |awk '{print $1}'
}
if [ $# -eq 1 ] ; then
    if [ $1 = 'ls' ] ; then
        grep -v "^#" ssh_list |awk '{print $1}'
    else
        name=$1
        line=`grep -v "^#" ssh_list |grep $name`
        #if [ `echo $line |wc -l` -ne 1 ] ; then
        if [ "z$line" = 'z' ] ; then
            usage $0
            exit 1
        fi
        user=`echo $line|awk '{print $2}'`
        ip=`echo $line|awk '{print $3}'`
        passwd=`echo $line|awk '{print $4}'`
        ./autossh $user $passwd $ip
    fi
else
    usage $0
    exit 1
fi
