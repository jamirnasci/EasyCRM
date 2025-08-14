from flask import Flask, redirect, url_for, flash
from flask_jwt_extended import JWTManager
from controller.login_controller import auth_bp
from controller.user_controller import user_bp
from controller.client_controller import client_bp
from controller.index_controller import index_bp
from controller.task_controller import task_bp
from datetime import timedelta
from flask_cors import CORS

from models.Client import Client
from models.Interaction import Interaction
from models.Task import Task
from models.User import User
from config.mysql import db

#db.connect()
#db.create_tables([Client, Interaction, Task, User])

app = Flask(__name__)
CORS(app)

app.config["JWT_SECRET_KEY"] = '302m1kj1sj21j09s'
app.config["JWT_TOKEN_LOCATION"] = "cookies"
app.config["JWT_COOKIE_SECURE"] = False
app.config["JWT_COOKIE_CSRF_PROTECT"] = False
app.config["JWT_ACCESS_COOKIE_PATH"] = "/"
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=5)

jwt = JWTManager(app)

app.register_blueprint(auth_bp, url_prefix="/auth")
app.register_blueprint(index_bp)
app.register_blueprint(user_bp, url_prefix="/user")
app.register_blueprint(client_bp, url_prefix="/client")
app.register_blueprint(task_bp, url_prefix="/task")

@jwt.unauthorized_loader
def unauthorized_response(e):
    return redirect(url_for('auth.login')), 302 # 302 Found para redirecionamento temporário

@jwt.invalid_token_loader
def invalid_token_response(e):
    return redirect(url_for('auth.login')), 302

@jwt.expired_token_loader
def expired_token_response(e):
    return redirect(url_for('auth.login')), 302

if __name__ == '__main__':
    app.run(debug=True)