function searchHandler(){
	const search = document.getElementById('search').value
    if(search && search.length > 3){
        location.href = "/adm/home?name=" + search
        return
    }
    alert('Insira o nome do usuario que deseja buscar com no mínimo 4 caracteres')
}