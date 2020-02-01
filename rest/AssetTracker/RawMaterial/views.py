from django.shortcuts import render
from django.http import HttpResponse
from rest_framework import viewsets
from . models import RawMaterial
from . serializer import RMSerializer


class RMView(viewsets.ModelViewSet):
    queryset = RawMaterial.objects.all()
    serializer_class = RMSerializer
