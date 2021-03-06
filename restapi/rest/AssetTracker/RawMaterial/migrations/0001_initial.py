# Generated by Django 2.0 on 2020-02-01 15:02

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='RawMaterial',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=20)),
                ('quantity', models.IntegerField()),
                ('packageID', models.IntegerField()),
                ('supplierAdd', models.CharField(max_length=50)),
                ('transporterAdd', models.CharField(max_length=50)),
                ('manuAdd', models.CharField(max_length=50)),
            ],
        ),
    ]
