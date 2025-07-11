from flask import Blueprint, render_template, url_for, redirect, request,  jsonify
from models.Task import Task
from flask_jwt_extended import get_jwt_identity, jwt_required
from models.Client import Client
from models.Task import Task
from models.User import User

task_bp = Blueprint('task', __name__)

@task_bp.route('/create', methods=['GET', 'POST'])
@jwt_required()
def create_task():
    if request.method == 'POST':
        identity = get_jwt_identity()
        title = request.form['title']
        due_date = request.form['due_date']
        status = request.form['status']
        description = request.form['description']
        client_id = request.form['client_id']

        try:
            Task(
                title = title, due_date = due_date,
                status = status, description = description,
                user = identity, client = client_id
            ).save()
        except Exception as e:
            print(e)
        
    client_list = Client.select()
    return render_template('tasks/create_task.jinja', clients=client_list)

@task_bp.route('/today', methods=['GET'])
@jwt_required()
def today():
    today_tasks = Task.select().join(User, on=(User.id == Task.user_id)).join(Client, on=((Client.id == Task.client_id) & (Client.user_id == get_jwt_identity())))
    return render_template('tasks/today.jinja', tasks=today_tasks)

@task_bp.route('/update/<int:id>', methods=['PUT'])
@jwt_required()
def update_task(id):
    data = request.get_json()
    status = data.get('status')
    try:
        Task.update(status = status).where(Task.id == id).execute()
        return jsonify({'msg': 'Task updated !'})
    except Exception as e:
        print(e)


@task_bp.route('/details/<int:id>', methods=['GET'])
@jwt_required()
def task_details(id):
    task = Task.select().join(User, on=(User.id == Task.user_id)) \
                .join(Client, on=(Client.id == Task.client_id)) \
                .where(Task.id == id) \
                .get()
    return render_template('tasks/task_details.jinja', task=task)
