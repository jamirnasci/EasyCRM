let form = document.getElementById('task-form')

form.addEventListener('submit', async (ev)=>{
    ev.preventDefault()

    try {
        const res = await fetch(`/task/create`, {
            method: 'POST',
            headers:{
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                due_date: document.getElementById('due_date').value,
                status: document.getElementById('status').value,
                description: document.getElementById('description').value,
                client_id: document.getElementById('client_id').value
            })
        })
        const obj = await res.json()
        alert(obj.msg)
    } catch (error) {
        console.log(`Erro ao criar cliente: ${error}`)
    }
})