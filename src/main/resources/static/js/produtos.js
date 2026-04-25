async function deleteHandler(tag){
	const produto = tag.getAttribute("productname")
	const id = tag.getAttribute("idproduct")
	
	if(!produto || !id){
		return
	}
	
	if(confirm("Deseja remover o produto " + produto)){
		const result = await fetch("/produtos/delete/" + id, {
			method: 'DELETE'
		})
		if(!result.ok){
			alert('Falha ao remover produto')
		}
		
	}
	location.reload()
}