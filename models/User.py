from config.mysql import db
from peewee import CharField, DateTimeField, Model
from models.BaseModel import BaseModel
from datetime import datetime

class User(BaseModel):
    name = CharField(max_length=100)
    email = CharField(max_length=150, unique=True)
    password_hash = CharField(max_length=255)
    created_at = DateTimeField(default=datetime.now)

    class Meta:
        table_name = 'users'
