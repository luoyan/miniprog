#!/bin/bash
function usage()
{
    echo "$0 create site"
    echo "$0 start site"
}
if [ $# -ne 2 ] ; then
    usage $0
    exit -1
fi
site=$2
function create()
{
    django-admin.py startproject $site
}
function start()
{
    cd $site
    python manage.py runserver 0.0.0.0:8080
    cd -
}
function sql()
{
    cd $site
    python manage.py sql jobs
    cd -
}
$@
