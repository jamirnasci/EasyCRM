const valorFinal = document.getElementById('valorFinal')
const productsSelect = document.getElementById('productsSelect')
const calcBtn = document.getElementById('calcBtn')
const quantity = document.getElementById('quantity')

/**
 * @type {{
 *      name: string,
 *      price: number,
 *      description: string,
 *      quantity: number
 * }}
 */
let loadedProduct = null
async function findProduct(){
    
    const result = await fetch('/produtos/findone/' + productsSelect.value, {
        method: 'GET'
    })
    loadedProduct = await result.json()
    valorFinal.value = Number(loadedProduct.price)
}

productsSelect.addEventListener('change', findProduct)

quantity.addEventListener('change', ()=>{
    valorFinal.value = (Number(loadedProduct.price) * Number(quantity.value)).toFixed(2)
})

const d1 = document.getElementById('date1')
const d2 = document.getElementById('date2')
const filterBtn = document.getElementById('filterBtn')

function filterByDates(){
    if(!d1.value || !d2.value){
        alert('Selecione as datas para a filtragem')
        return
    }
    let url = `/vendas?d1=${d1.value}&d2=${d2.value}`
    location.href = url
}

filterBtn.addEventListener('click', filterByDates)