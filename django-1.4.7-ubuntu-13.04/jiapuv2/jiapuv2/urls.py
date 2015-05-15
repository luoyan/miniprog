from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    url(r'^search', 'jiapu.views.search', name='search'),
    url(r'^list', 'jiapu.views.list', name='list'),
    url(r'^get_person', 'jiapu.views.get_person', name='get_person'),
    # Examples:
    # url(r'^$', 'jiapuv2.views.home', name='home'),
    # url(r'^jiapuv2/', include('jiapuv2.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
