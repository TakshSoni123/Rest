from django.urls import path, include
from . views import MedView
from rest_framework import routers

router = routers.DefaultRouter()
router.register('medicine', MedView, basename='medicine')

urlpatterns = [
    path('', include(router.urls))
]
