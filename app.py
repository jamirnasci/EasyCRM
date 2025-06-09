from flask import Flask
from flask_jwt_extended import JWTManager
from controller.login_controller import login_bp
from controller.user_controller import user_bp
from controller.client_controller import client_bp
from controller.index_controller import index_bp
from controller.task_controller import task_bp
from datetime import timedelta

app = Flask(__name__)

app.config["JWT_SECRET_KEY"] = '302m1kj1sj21j09s'
app.config["JWT_TOKEN_LOCATION"] = "cookies"
app.config["JWT_COOKIE_SECURE"] = False
app.config["JWT_COOKIE_CSRF_PROTECT"] = False
app.config["JWT_ACCESS_COOKIE_PATH"] = "/"
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(hours=5)

jwt = JWTManager(app)

app.register_blueprint(login_bp)
app.register_blueprint(index_bp)
app.register_blueprint(user_bp)
app.register_blueprint(client_bp)
app.register_blueprint(task_bp)

if __name__ == '__main__':
    app.run(debug=True)