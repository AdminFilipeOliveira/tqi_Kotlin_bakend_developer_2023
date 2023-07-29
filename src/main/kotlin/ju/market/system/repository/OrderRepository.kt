package ju.market.app.repository

import ju.market.app.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.math.BigDecimal

//Queries para a tabela 'pedidos'
@Repository
interface OrderRepository: JpaRepository<Order, Long> {

    //SELECIONA todos os campos dos pedidos de acordo com o código de venda
    @Query(value = "SELECT * FROM order WHERE vending_code = :code", nativeQuery = true)
    fun findByVendingCode(@Param("code") code: String): List<Order>

    //Retorna o valor total dos pedidos de acordo com o código de venda
    @Query(value = "SELECT SUM(unit_price) as total_price FROM order WHERE vending_code = :code", nativeQuery = true)
    fun findTotalByVendingCode(@Param("code") code: String): BigDecimal
}