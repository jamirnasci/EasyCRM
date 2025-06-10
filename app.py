from flask import Flask, redirect, url_for, flash
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

@jwt.unauthorized_loader
def unauthorized_response(e):

    #flash('Por favor, faça login para aceder a esta página.', 'warning')
    return redirect(url_for('login.login')), 302 # 302 Found para redirecionamento temporário

@jwt.invalid_token_loader
def invalid_token_response(e):

    #flash('O seu token de acesso é inválido. Por favor, faça login novamente.', 'danger')
    return redirect(url_for('login.login')), 302

@jwt.expired_token_loader
def expired_token_response(e):

    #flash('A sua sessão expirou. Por favor, faça login novamente.', 'info')
    return redirect(url_for('login.login')), 302

if __name__ == '__main__':
    app.run(debug=True)