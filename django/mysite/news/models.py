from django.db import models

# Create your models here.
class Reporter(models.Model):
    full_name = models.CharField(max_length=70)

    def __unicode__(self):
        return self.full_name

class Article(models.Model):
    pub_date = models.DateField()
    headline = models.CharField(max_length=200)
    content = models.TextField()
    report = models.ForeignKey(Reporter)

    def __unicode__(self):
        return self.headline
