async function deleteHandler(tag){
	const cliente = tag.getAttribute("customername")
	const id = tag.getAttribute("idcustomer")
	
	if(!cliente || !id){
		return
	}
	
	if(confirm("Deseja remover o cliente " + cliente)){
		const result = await fetch("/clientes/delete/" + id, {
			method: 'DELETE'
		})
		if(!result.ok){
			alert('Falha ao remover cliente')
		}
		
	}
	location.reload()
}

function searchHandler(){
	const search = document.getElementById('search').value
	location.href = "/clientes?search=" + search
}