package ju.market.app.finalBay

//Classe usada para retornar as informações para o cliente após a compra dos produtos.
data class FinalVendingResponse(
    val paymentForm: String,
    val totalValue: String,
    val vendingCode: String,
    val mensage: String
)
