from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    #url(r'^$', 'display.views.home', name='home'),
    url(r'^$', 'display.views.simple_home', name='simple_home'),
    url(r'^get_relationship$', 'display.views.get_relationship', name='get_relationship'),
    url(r'^get_person_info$', 'display.views.get_person_info', name='get_person_info'),
    # url(r'^jiapu/', include('jiapu.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
