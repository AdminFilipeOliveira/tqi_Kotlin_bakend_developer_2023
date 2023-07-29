package ju.market.app.dto

import ju.market.app.entity.Category
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


//Informações que serão passadas via JSON para manipular as categorias. Validações pelo Hibernate
data class CategoryDto(
    @field:NotEmpty
    @field:Size(max = 30)
    @field:Pattern(regexp = "[\\p{L}]+")
    val name: String = ""
){
    fun toEntity(): Category = Category(
        name = this.name
    )
}
