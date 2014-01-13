#!/bin/bash
function usage()
{
    echo "$0 create site"
    echo "$0 start site"
}
if [ $# -lt 2 ] ; then
    usage $0
    exit -1
fi
site=$2
app=jobs
if [ $# -eq 3 ] ; then
    app=$3
fi
function create()
{
    django-admin.py startproject $site
}
function start()
{
    cd $site
    #python manage.py runserver 0.0.0.0:8080
    #python manage.py runserver 0.0.0.0:80
    python manage.py runserver 0.0.0.0:3389
    cd -
}
function sql()
{
    cd $site
    python manage.py sql $app
    cd -
}
function startapp()
{
    cd $site
    python manage.py startapp $app
    cd -
}
$@
#django-amdin.py startproject booksearch
#python manage.py startapp search
#python runserver 0.0.0.0:3389
