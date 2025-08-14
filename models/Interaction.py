from peewee import ForeignKeyField, CharField, DateTimeField, TextField
from datetime import datetime

from models.BaseModel import BaseModel
from models.Client import Client
from models.User import User

class Interaction(BaseModel):
    client = ForeignKeyField(Client, backref='interactions', on_delete='CASCADE')
    user = ForeignKeyField(User, backref='interactions', on_delete='CASCADE')
    type = CharField(choices=[('call', 'call'), ('email', 'email'), ('meeting', 'meeting'), ('other', 'other')], default='other')
    contact_date = DateTimeField()
    notes = TextField(null=True)
    created_at = DateTimeField(default=datetime.now)
