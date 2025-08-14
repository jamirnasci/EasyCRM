const updateBtn = document.getElementById('update-btn')

updateBtn.addEventListener('click', async(event)=>{
    const id = event.target.getAttribute('taskId')
    const res = await fetch(`/task/update/${id}`, {
        method: 'PUT',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify({
            status: document.getElementById('status').value
        })
    })
    if (res.status === 200) {
        const obj = await res.json()
        alert(obj.msg)
    } else {
        alert('Fail to update Task')
    }
}) 