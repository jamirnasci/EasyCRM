from peewee import ForeignKeyField, CharField, DateField, TextField, DateTimeField
from datetime import datetime

from models.BaseModel import BaseModel
from models.Client import Client
from models.User import User

class Task(BaseModel):
    client = ForeignKeyField(Client, backref='tasks', on_delete='CASCADE')
    user = ForeignKeyField(User, backref='tasks', on_delete='CASCADE')
    title = CharField(max_length=150)
    description = TextField(null=True)
    due_date = DateField(null=True)
    status = CharField(choices=[('pending', 'pending'), ('completed', 'completed')], default='pending')
    created_at = DateTimeField(default=datetime.now)

    class Meta:
        table_name = 'tasks'