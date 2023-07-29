package ju.market.app.dto

import ju.market.app.entity.Cart
import jakarta.validation.constraints.*


//Informações que serão passadas via JSON para manipular o carrinho. Validações pelo Hibernate
class CartDto (
    @field:NotEmpty
    @field:Size(max = 30)
    val productName: String = "",
    @field:Min(1)
    val idProduct: Long,
    @field:NotNull
    @field:Min(1)
    val productQuatity: Int,
){
    fun toEntity(): Cart = Cart(
        idProduct = this.idProduct,
        productName = this.productName,
        productQuatity = this.productQuatity,
    )

}