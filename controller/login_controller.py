from flask import Blueprint, render_template, request, redirect, url_for
from models.User import User
from werkzeug.security import generate_password_hash, check_password_hash
from flask_jwt_extended import create_access_token, set_access_cookies, unset_access_cookies, jwt_required

auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        user = User.get_or_none(User.email == email)
        if user:
            if check_password_hash(user.password_hash, password):
                print(user.id)
                token = create_access_token(identity=str(user.id))
                response = redirect(url_for('client.clients'))
                set_access_cookies(response, token)
                return response
            else:
                return redirect(url_for('auth.login'))
    
    return render_template('login.html')

@auth_bp.route('/logout')
@jwt_required()
def logout():
    response = redirect(url_for('auth.login'))
    unset_access_cookies(response)
    return response