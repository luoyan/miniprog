create()
{
    django-admin.py startproject mysite
}
start()
{
    cd mysite
    python manage.py runserver 0.0.0.0:8080
}
$@
