package ju.market.app.service


import ju.market.app.entity.Product
import java.util.*

//Implementações
interface IProductService {
    fun save(product: Product): Product
    fun findAllByCategory(categoryId: Long): List<Product>
    fun findById(id: Long): Optional<Product>
    fun findAllProduct(): List<Product>
    fun findAllProductByName(name: String): List<Product>
    fun editProduct(product: Product): String
    fun delete(id: Long)
}