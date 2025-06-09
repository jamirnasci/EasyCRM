from peewee import ForeignKeyField, CharField, DateTimeField
import datetime

from models import BaseModel, User

class Client(BaseModel):
    user = ForeignKeyField(User, backref='clients', on_delete='CASCADE')
    name = CharField(max_length=100)
    email = CharField(max_length=150, null=True)
    phone = CharField(max_length=20, null=True)
    company = CharField(max_length=100, null=True)
    position = CharField(max_length=100, null=True)
    first_contact_date = DateTimeField(default=datetime.now)
    status = CharField(choices=[('lead', 'lead'), ('opportunity', 'opportunity'), ('client', 'client'), ('lost', 'lost')], default='lead')
    created_at = DateTimeField(default=datetime.now)
