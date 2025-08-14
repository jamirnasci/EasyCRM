let form = document.getElementById('client-from')

form.addEventListener('submit', async (ev)=>{
    ev.preventDefault()

    try {
        const res = await fetch('/client/create', {
            method: 'POST',
            headers:{
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: document.getElementById('name').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value,
                company: document.getElementById('company').value,
                position: document.getElementById('position').value,
                notes: document.getElementById('notes').value,
                status: document.getElementById('status').value,
            })
        })
        const obj = await res.json()
        alert(obj.msg)
    } catch (error) {
        console.log(`Erro ao criar cliente: ${error}`)
    }
})