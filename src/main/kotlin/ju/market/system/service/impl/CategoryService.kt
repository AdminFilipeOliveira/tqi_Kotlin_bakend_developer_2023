package ju.market.app.service.impl

import ju.market.app.entity.Category
import ju.market.app.repository.CategoryRepository
import ju.market.app.service.ICategoryService
import org.springframework.stereotype.Service


//Regras de negócio para a categoria
@Service
class CategoryService(private val categoryRepository: CategoryRepository): ICategoryService {

    //Salva uma nova categoria
    override fun save(category: Category): Category =
        this.categoryRepository.save(category)

    //Encontra uma categoria pelo ID
    override fun findById(id: Long): Category =
        this.categoryRepository.findById(id).orElseThrow{
            throw RuntimeException("Id $id não encontrado.")
        }

    //Retorna todas as categorias
    override fun findAllCategory(): List<Category> =
        this.categoryRepository.findAll()

    //Deleta uma categoria pelo ID
    override fun deleteById(id: Long) {
        this.categoryRepository.deleteById(id)
    }

    //Deleta todas as categorias
    override fun deleteAll() {
       this.categoryRepository.deleteAll()
    }
}