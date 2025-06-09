from peewee import ForeignKeyField, CharField, DateTimeField, PrimaryKeyField
from datetime import datetime

from models.BaseModel import BaseModel
from models.User import User

class Client(BaseModel):
    id = PrimaryKeyField()
    user_id = ForeignKeyField(User, backref='clients', on_delete='CASCADE')
    name = CharField(max_length=100)
    email = CharField(max_length=150, null=True)
    phone = CharField(max_length=20, null=True)
    company = CharField(max_length=100, null=True)
    position = CharField(max_length=100, null=True)
    created_at = DateTimeField(default=datetime.now)
    status = CharField(choices=[('lead', 'lead'), ('opportunity', 'opportunity'), ('client', 'client'), ('lost', 'lost')], default='lead')

    class Meta:
        table_name = 'clients'