from django.shortcuts import render
from django.http import HttpResponse
from rest_framework import viewsets
from . models import Medicine
from . serializer import MedSerializer


class MedView(viewsets.ModelViewSet):
    queryset = Medicine.objects.all()
    serializer_class = MedSerializer
