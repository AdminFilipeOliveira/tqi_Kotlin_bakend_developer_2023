package ju.market.app.dto

import ju.market.app.entity.Category
import ju.market.app.entity.Product
import jakarta.validation.constraints.*
import ju.market.system.enumm.UnitMeasure
import java.math.BigDecimal

//Informações que serão passadas via JSON para alterar um produto. Validações via Hibernate.
data class ChangeProductDto(
    val id: Long,
    @field:NotEmpty
    @field:Size(max = 30)
    val name: String = "",
    @field:NotNull
    val unitOfMeasure: UnitMeasure,
    @field:Min(0)
    val unitPrice: BigDecimal,
    @field:Min(1)
    val categoryId: Long,
    @field:Min(1)
    val quatity: Int,
){
    fun toEntity(): Product = Product(
        id = this.id,
        name = this.name,
        unitOfMeasure = this.unitOfMeasure,
        unitPrice = this.unitPrice,
        category = Category(id = this.categoryId),
        quatity = this.quatity
    )
}
