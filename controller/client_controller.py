from flask import Blueprint, render_template, request, redirect, url_for, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity

client_bp = Blueprint('client', __name__)

@client_bp.route('/clients')
@jwt_required()
def clients():
    identity = get_jwt_identity()
    return jsonify({'identity': identity})