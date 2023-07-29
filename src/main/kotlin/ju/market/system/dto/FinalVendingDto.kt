package ju.market.app.dto

import ju.market.app.enumm.PaymentForm
import jakarta.validation.constraints.NotNull


//Informação que será passada via JSON para concluir o pedido. Validação via Hibernate
class FinalVendingDto (
    @field:NotNull
    val paymentForm: PaymentForm
)


