from peewee import ForeignKeyField, CharField, DateField, TextField, DateTimeField
import datetime

from models import BaseModel, Client, User

class Task(BaseModel):
    client = ForeignKeyField(Client, backref='tasks', on_delete='CASCADE')
    user = ForeignKeyField(User, backref='tasks', on_delete='CASCADE')
    title = CharField(max_length=150)
    description = TextField(null=True)
    due_date = DateField(null=True)
    status = CharField(choices=[('pending', 'pending'), ('completed', 'completed')], default='pending')
    created_at = DateTimeField(default=datetime.now)
