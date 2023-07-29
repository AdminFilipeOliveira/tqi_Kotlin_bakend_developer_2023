package ju.market.app.service

import ju.market.app.entity.Category


//Implementações
interface ICategoryService {
    fun save(category: Category): Category
    fun findById(id: Long): Category
    fun findAllCategory(): List<Category>
    fun deleteById(id: Long)
    fun deleteAll()
}