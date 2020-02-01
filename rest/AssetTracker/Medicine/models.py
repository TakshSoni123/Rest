from django.db import models


class Medicine(models.Model):

    name = models.CharField(max_length=20)
    quantity = models.IntegerField()
    packageID = models.IntegerField()
    desc = models.CharField(max_length=100)
    manuAdd = models.CharField(max_length=50)
    transporterAdd = models.CharField(max_length=50)
    pharmAdd = models.CharField(max_length=50)

    def __str__(self):
        return self.name