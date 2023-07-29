package ju.market.app.repository

import ju.market.app.entity.Cart
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

import java.math.BigDecimal

//Queries para a tabela 'carrinho'
@Repository
interface CartRepository: JpaRepository<Cart, Long> {

    //SELECIONA todos os produtos do carrinho
    @Query(value = "SELECT * FROM cart", nativeQuery = true)
    fun findAllProduct(): List<Cart>

    //Altera um produto do carrinho
    @Transactional
    @Modifying
    @Query(value = "UPDATE cart SET product_quatity = :productQuantity, product_price = :productPrice WHERE product_name = :productName", nativeQuery = true)
    fun update(@Param("productName") productName: String, @Param("productQuatity") productQuatity: Int, @Param("productPrice") productPrice: BigDecimal)

    //Faz um TRUNCATE na tabela do carrinho após cada compra para resetar os IDs
    @Transactional
    @Modifying
    @Query("TRUNCATE TABLE cart", nativeQuery = true)
    fun truncateAll()

    //Checa se o produto já existe no carrinho
    @Nullable //Para corrigir o erro de retornar null
    @Query("SELECT id_product FROM cart WHERE id_product = :productId", nativeQuery = true)
    fun existsCart(@Param("productId") productId: Long): Long?
}