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
create()
{
    django-admin.py startproject $site
}
start()
{
    cd $site
    python manage.py runserver 0.0.0.0:8080
}
$@
