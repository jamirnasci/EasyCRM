from flask import Blueprint, render_template, request, redirect, url_for
from models.User import User
from werkzeug.security import generate_password_hash, check_password_hash
from flask_jwt_extended import create_access_token, set_access_cookies

login_bp = Blueprint('login', __name__)

@login_bp.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        user = User.get_or_none(User.email == email)
        if user:
            if check_password_hash(user.password_hash, password):
                token = create_access_token(identity=user.email)
                response = redirect(url_for('client.clients'))
                set_access_cookies(response, token)
                return response
            else:
                return redirect(url_for('login.login'))
    
    return render_template('login.html')



    
    return render_template('login.html')