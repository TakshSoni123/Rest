from rest_framework import serializers
from . models import Medicine


class MedSerializer(serializers.ModelSerializer):
    class Meta:
        model = Medicine
        fields = '__all__'