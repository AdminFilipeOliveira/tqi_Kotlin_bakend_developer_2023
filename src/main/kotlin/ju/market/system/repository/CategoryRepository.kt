package ju.market.app.repository

import ju.market.app.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


//Queries para a tabela 'categoria'
@Repository
interface CategoryRepository: JpaRepository<Category, Long> {

/*
Não há métodos personalizados aqui, somente os próprios do JPA
 */

}