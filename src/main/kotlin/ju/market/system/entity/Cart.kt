package ju.market.app.entity

import jakarta.persistence.*
import java.math.BigDecimal

//Tabela do carrinho que será criada no banco de dados
@Entity
@Table(name = "carrinho")
data class Cart(
    //ID que é gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val idProduct: Long,
    @Column(nullable = false, length = 20) val productName: String,
    @Column(nullable = false) var productQuatity: Int,
    @Column(nullable = false) var productPrice: BigDecimal = BigDecimal.ZERO,
)
