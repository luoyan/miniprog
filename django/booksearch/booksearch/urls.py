from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'search.views.home', name='home'),
    url(r'^hello/$', 'search.views.hello', name='hello'),
    url(r'^time/$', 'search.views.current_datetime', name='current_datetime'),
    url(r'^render_template$', 'search.views.render_template', name='render_template'),
    url(r'^index/$', 'search.views.index', name='index'),
    url(r'^images/', 'search.views.images_files', name='images_files'),
    # Examples:
    # url(r'^$', 'booksearch.views.home', name='home'),
    # url(r'^booksearch/', include('booksearch.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
) #+ static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
