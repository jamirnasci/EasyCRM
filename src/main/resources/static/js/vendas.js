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
    valorFinal.value = Number(loadedProduct.price) * Number(quantity.value)
})