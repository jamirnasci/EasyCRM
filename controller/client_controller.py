from flask import Blueprint, render_template, request, redirect, url_for, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from models.Client import Client
from playhouse.shortcuts import model_to_dict

client_bp = Blueprint('client', __name__)

@client_bp.route('/clients')
@jwt_required()
def clients():
    identity = get_jwt_identity()
    client_list = Client.select()
    return render_template('client/client_list.jinja', clients=client_list)

@client_bp.route('/create', methods=['POST', 'GET'])
@jwt_required()
def create_client():
    if request.method == 'POST':
        json = request.get_json()
        user_id = get_jwt_identity()
        name = json.get('name')
        email = json.get('email')
        phone = json.get('phone')
        company = json.get('company')
        position = json.get('position')
        status = json.get('status')
        
        try:
            Client(
                name = name, email = email,
                phone = phone, company = company,
                position = position, status = status,
                user_id = user_id
            ).save()
            return jsonify({'msg': 'Cliente cadastrado'}), 200
        except Exception as e:
            print(e)
            return jsonify({'msg': 'Falha ao cadastrar cliente'}), 400
    return render_template('client/create_client.jinja')

@client_bp.route('/details/<int:id>')
@jwt_required()
def client_details(id):
    client = Client.get((Client.id == id) & (Client.user_id == get_jwt_identity()))
    if client:
        return render_template('client/client_details.jinja', client=client)
    return jsonify({'msg': 'Client not found !'})

@client_bp.route('/update/<int:id>', methods=['POST'])
@jwt_required()
def update_client(id):
    json = request.get_json()
    name = json.get('name')
    email = json.get('email')
    phone = json.get('phone')
    company = json.get('company')
    position = json.get('position')
    status = json.get('status')
    try:
        Client.update(                
            name = name, email = email,
            phone = phone, company = company,
            position = position, status = status,
        ).where(Client.id == id).execute()
        return jsonify({'msg': 'Cliente atualizado'}), 200
    except Exception as e:
        print(e)
        return jsonify({'msg': 'Falha ao atualizar clientes'}), 400

@client_bp.route('/delete/<int:id>', methods=['GET'])
@jwt_required()
def delete_client(id):
    try:
        Client.delete_by_id(id)
        return redirect(url_for('client.clients'))
    except Exception as e:
        print(e)
        return redirect(url_for('client.client_details', id=id))
    
@client_bp.route('/findbyname', methods=['POST'])
@jwt_required()
def find_by_name():
    data = request.get_json()
    nome = data.get('nome')
    
    try:
        clientes = Client.select().where(Client.name.contains(nome.lower()))
        dict = [model_to_dict(c) for c in clientes]
        return jsonify(dict), 200
    except Exception as e:
        print(e)
        return jsonify([]), 400
