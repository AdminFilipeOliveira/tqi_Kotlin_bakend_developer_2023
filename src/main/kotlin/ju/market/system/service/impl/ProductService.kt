package ju.market.app.service.impl

import ju.market.app.entity.Product
import ju.market.app.repository.CategoryRepository
import ju.market.app.repository.ProductRepository
import ju.market.app.service.IProductService
import org.springframework.stereotype.Service
import java.util.*


//Regras de negócio para os produtos
@Service
class ProductService(private val productRepository: ProductRepository, private val categoryRepository: CategoryRepository):
    IProductService {

    override fun save(product: Product): Product {
        val exists: Boolean = this.categoryRepository.existsById(product.category.id!!)
        if(exists) return this.productRepository.save(product)
        else throw IllegalArgumentException("Categoria não existe.")
    }

    override fun findAllByCategory(categoryId: Long): List<Product> {
        return this.productRepository.findAllByCategory(categoryId)
    }

    override fun findById(id: Long): Optional<Product> {
        return this.productRepository.findById(id)
    }

    override fun findAllProduct(): List<Product> {
        return this.productRepository.findAll()
    }

    override fun findAllProductByName(name: String): List<Product> {
        return this.productRepository.findAllByName(name)
    }

    override fun editProduct(product: Product): String {
        val status: Int = this.productRepository.editProduct(product.id!!, product.name,
            product.unitOfMeasure.name, product.unitPrice, product.category.id!!, product.quatity)
        if(status == 1){
            return "O produto de ID: ${product.id} foi alterado com sucesso!"
        }else{
            return "Produto não encontrado."
        }
    }

    override fun delete(id: Long) {
        this.productRepository.deleteById(id)
    }
}