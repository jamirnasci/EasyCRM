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

function searchHandler(){
	const search = document.getElementById('search').value
	const category = document.getElementById('category').value
	let url = "/produtos?"
	if(!search && !category){
		alert('Digite um nome ou categoria para pesquisa')
		return
	}
	if(search){
		url += "search=" + search
	}
	if(category){
		url += `${search ? '&' : ''}category=${category}`
	}
	location.href = url
}

const filterBtn = document.getElementById('filterBtn')
filterBtn.addEventListener('click', searchHandler)