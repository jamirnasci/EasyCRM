from flask import Blueprint, request, redirect, url_for, render_template
from models.User import User
from werkzeug.security import generate_password_hash
from flask_jwt_extended import jwt_required

user_bp = Blueprint('user', __name__)

@user_bp.route('/create', methods=['GET', 'POST'])
def create_user():
    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        password = request.form['password']
        
        if name and password:
            password = generate_password_hash(password)

            try:
                User(name=name, email=email, password_hash=password).save()
                return redirect(url_for('auth.login'))
            except Exception as e:
                print(e)
    return render_template('create_user.jinja')

