from django.db import models


class RawMaterial(models.Model):

    name = models.CharField(max_length=20)
    quantity = models.IntegerField()
    packageID = models.IntegerField()

    supplierAdd = models.CharField(max_length=50)
    transporterAdd = models.CharField(max_length=50)
    manuAdd = models.CharField(max_length=50)

    def __str__(self):
        return self.name

