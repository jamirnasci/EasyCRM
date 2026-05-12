const quantity = document.getElementById('quantity')
const valorFinal = document.getElementById('valorFinal')
const unityPrice = document.getElementById('precoUnitario')

quantity.addEventListener('change', ()=>{
    valorFinal.value = (Number(unityPrice.innerHTML) * Number(quantity.value)).toFixed(2)
})