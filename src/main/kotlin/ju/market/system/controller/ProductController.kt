package ju.market.app.controller


import ju.market.app.dto.ChangeProductDto
import ju.market.app.dto.ProductDto
import ju.market.app.entity.Product
import ju.market.app.service.impl.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/produtos")
class ProductController(private val productService: ProductService) {

    //Salva um produto novo
    @PostMapping
    fun saveProduct(@RequestBody @Valid productDto: ProductDto): ResponseEntity<String> {
        return try {
            val product: Product = this.productService.save(productDto.toEntity())
            val response: String = "O produto '${product.name}' foi adicionado com sucesso!"
            ResponseEntity.status(HttpStatus.OK).body(response)
        }catch(e: IllegalArgumentException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    //Retorna todos os produtos por nome, categoria ou sem condições
    @GetMapping
    fun getAllProduct(@RequestParam(required = false) categoryId: Long?,
                       @RequestParam(required = false, defaultValue = "") name: String): ResponseEntity<Optional<List<Product>>> {
        //Se o parâmetro da categoria existir
        if(categoryId != null && categoryId > 0) return ResponseEntity.ok().body(Optional.of(this.productService.findAllByCategory(categoryId)))

        //Se o parâmetro do nome não estiver vazio
        if(name.isNotEmpty()) return ResponseEntity.ok().body(Optional.of(this.productService.findAllProductByName(name)))

        //Se nenhuma condição for mencionada
        val product: List<Product> = this.productService.findAllProduct()
        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(product))
    }

    //Retorna um produto pelo ID
    @GetMapping("/s")
    fun getProductById(@RequestParam("id") @Valid id: Long): ResponseEntity<Optional<Product>> {
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.findById(id))

    }

    //Altera um produto
    @PutMapping("/alterar")
    fun editProduct(@RequestBody @Valid changeProductDto: ChangeProductDto): ResponseEntity<String> {
        val response: String = productService.editProduct(changeProductDto.toEntity())
        if(response == "Produto não encontrado.") return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        else return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Deleta um produto pelo ID
    @DeleteMapping("/{id}")
    fun deleteProductById(@PathVariable id: Long): ResponseEntity<String> {
        this.productService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body("Produto de ID:$id excluído com sucesso.")
    }

}