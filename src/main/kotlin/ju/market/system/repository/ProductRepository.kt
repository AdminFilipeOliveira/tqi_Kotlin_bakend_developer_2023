package ju.market.app.repository

import ju.market.app.entity.Product
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.math.BigDecimal

//Queries para a tabela 'produtos'
@Repository
interface ProductRepository: JpaRepository<Product, Long> {

    //SELECIONA todos os campos dos produtos de acordo com o ID da categoria
    @Query(value = "SELECT * FROM product WHERE category_id = :category", nativeQuery = true)
    fun findAllByCategory(@Param("categoria") category: Long): List<Product>

    //SELECIONA todos os campos dos produtos que contém o nome enviado
    @Query(value = "SELECT * FROM product WHERE name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    fun findAllByName(@Param("nome") name: String): List<Product>

    //Calcula o preco total dos produtos de acordo com a quantidade
    @Query(value = "SELECT SUM(unit_price * :quatity) as total_price FROM product WHERE id = :id AND name = :name", nativeQuery = true)
    fun calculatePrice(@Param("id") id: Long, @Param("name") name: String, @Param("quatity") quantity: Int): BigDecimal

    //Altera um produto
    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET name = :name, unit_Of_Measure = :unitOfMeasure, " +
            "unit_price = :unitPrice, category_id = :category, quatity = :quatity WHERE id = :id", nativeQuery = true)
    fun editProduct(@Param("id") id: Long,
                     @Param("nome") name: String,
                     @Param("unitOfMeasure") unitOfMeasure: String,
                     @Param("unitPrice") unitPrice: BigDecimal,
                     @Param("category") category: Long,
                     @Param("quatity") quatity: Int): Int

    //Verifica a quantidade de produtos pelo ID
    @Query(value = "SELECT quatity FROM product WHERE id = :id", nativeQuery = true)
    fun checkQntProduct(@Param("id") id: Long): Int

    //Remove o estoque dos produtos a cada compra finalizada
    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET quatity = quatity - :quatity WHERE id = :id", nativeQuery = true)
    fun removeProductStock(@Param("id") id: Long, @Param("quatity") quatity: Int)

}