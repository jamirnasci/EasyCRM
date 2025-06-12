from flask import Blueprint, render_template, request, redirect, url_for, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from models.Client import Client

client_bp = Blueprint('client', __name__)

@client_bp.route('/clients')
@jwt_required()
def clients():
    identity = get_jwt_identity()
    client_list = Client.select()
    return render_template('client/client_list.html', clients=client_list)

@client_bp.route('/create-client', methods=['POST', 'GET'])
@jwt_required()
def create_client():
    if request.method == 'POST':
        user_id = get_jwt_identity()
        name = request.form['name']
        email = request.form['email']
        phone = request.form['phone']
        company = request.form['company']
        position = request.form['position']
        status = request.form['status']
        
        try:
            Client(
                name = name, email = email,
                phone = phone, company = company,
                position = position, status = status,
                user_id = user_id
            ).save()
        except Exception as e:
            print(e)
    return render_template('client/create_client.html')

@client_bp.route('/client-details/<int:id>')
@jwt_required()
def client_details(id):
    client = Client.get((Client.id == id) & (Client.user_id == get_jwt_identity()))
    if client:
        return render_template('client/client_details.html', client=client)
    return jsonify({'msg': 'Client not found !'})

@client_bp.route('/update-client/<int:id>', methods=['POST'])
@jwt_required()
def update_client(id):
    name = request.form['name']
    email = request.form['email']
    phone = request.form['phone']
    company = request.form['company']
    position = request.form['position']
    status = request.form['status']
    try:
        Client.update(                
            name = name, email = email,
            phone = phone, company = company,
            position = position, status = status,
        ).where(Client.id == id).execute()
        return redirect(url_for('client.client_details', id=id))
    except Exception as e:
        print(e)