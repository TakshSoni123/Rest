from rest_framework import serializers
from . models import RawMaterial


class RMSerializer(serializers.ModelSerializer):
    class Meta:
        model = RawMaterial
        fields = '__all__'