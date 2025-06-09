from flask import Flask
from flask_jwt_extended import JWTManager
from controller.login_controller import login_bp
from controller.user_controller import user_bp
from controller.client_controller import client_bp
app = Flask(__name__)

app.config["JWT_SECRET_KEY"] = '302m1kj1sj21j09s'
app.config["JWT_TOKEN_LOCATION"] = "cookies"
app.config["JWT_COOKIE_SECURE"] = False
app.config["JWT_COOKIE_CSRF_PROTECT"] = False
app.config["JWT_ACCESS_COOKIE_PATH"] = "/"

jwt = JWTManager(app)

app.register_blueprint(login_bp)
app.register_blueprint(user_bp)
app.register_blueprint(client_bp)

if __name__ == '__main__':
    app.run(debug=True)