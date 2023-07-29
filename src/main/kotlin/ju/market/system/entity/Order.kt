package ju.market.app.entity

import ju.market.app.enumm.PaymentForm
import jakarta.persistence.*

import java.math.BigDecimal

//Tabela dos pedidos que será criada no banco de dados
@Entity
@Table(name = "pedidos")
data class Order(
    //ID será gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 30) val productName: String,
    @Column(nullable = false, length = 30) val idProduct: Long? = null,
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)") val unitPrice: BigDecimal,
    @Column(nullable = false) val quatity: Int,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false) val paymentForm: PaymentForm,
    @Column(nullable = false) val vendingCode: String
)
