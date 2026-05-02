const copyBtn = document.getElementById("copyProductDetailsBtn");

function copiarProduto() {
    const name = document.getElementById("p-name")?.innerText || "";
    const price = document.getElementById("p-price")?.innerText || "";
    const quantity = document.getElementById("p-quantity")?.innerText || "";
    const category = document.getElementById("p-category")?.innerText || "";
    const description = document.getElementById("p-description")?.innerText || "";
    const texto =
`📦 Produto: ${name}
💰 Preço: ${price}
📊 Estoque: ${quantity}
🏷️ Categoria: ${category}
📝 Descrição: ${description}`;

    navigator.clipboard.writeText(texto)
        .then(() => {
            alert("Informações copiadas!");
        })
        .catch(() => {
            alert("Erro ao copiar.");
        });
}

copyBtn.addEventListener("click", copiarProduto);
