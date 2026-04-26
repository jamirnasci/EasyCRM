const d1 = document.getElementById('date1')
const d2 = document.getElementById('date2')
const status = document.getElementById('status')
const filterBtn = document.getElementById('filterBtn')

function filterInteractions(){
    if(!d1.value || !d2.value){
        alert('Selecione as datas para a filtragem')
        return
    }
    let url = `/interacoes?d1=${d1.value}&d2=${d2.value}`
    if(status.value){
        url += `&status=${status.value}`
    }
    location.href = url
}

filterBtn.addEventListener('click', filterInteractions)