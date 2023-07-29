package ju.market.app.controller

import ju.market.app.dto.CategoryDto
import ju.market.app.entity.Category
import ju.market.app.service.impl.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 4600)
@RequestMapping("/api/categoria")
class CategoryController(private val categoryService: CategoryService) {

    //Salva a categoria
    @PostMapping
    fun saveCategory(@RequestBody @Valid categoryDto: CategoryDto): ResponseEntity<String> {
        val category: Category = this.categoryService.save(categoryDto.toEntity())

        //Resposta que vem na requisição
        val response: String = "A categoria '${category.name}' foi adicionada!"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Retorna uma categoria pelo ID
    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Optional<Category>> {
        val category: Optional<Category> = Optional.of(this.categoryService.findById(id))
        return ResponseEntity.status(HttpStatus.OK).body(category)
    }

    //Retorna todas as categorias
    @GetMapping
    fun getAllCategory(): ResponseEntity<Optional<List<Category>>> {
        val categorys: List<Category> = this.categoryService.findAllCategory()
        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(categorys))
    }

    //Deleta uma categoria pelo ID
    @DeleteMapping("/{id}")
    fun deleteCategoryById(@PathVariable id: Long): ResponseEntity<String> {
        this.categoryService.deleteById(id)
        val response = "Categoria de número $id deletada com sucesso"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Deleta todas as categorias
    @DeleteMapping
    fun deleteAllCategory(): ResponseEntity<String> {
        this.categoryService.deleteAll()
        val response = "Todas as categorias foram deletadas!"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}