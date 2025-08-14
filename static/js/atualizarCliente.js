let form = document.getElementById('client-form')

form.addEventListener('submit', async (ev)=>{
    ev.preventDefault()
    let id = form.getAttribute('idcliente')
    
    try {
        const res = await fetch(`/client/update/${id}`, {
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
                status: document.getElementById('status').value,
            })
        })
        const obj = await res.json()
        alert(obj.msg)
    } catch (error) {
        console.log(`Erro ao atualizar cliente: ${error}`)
    }
})