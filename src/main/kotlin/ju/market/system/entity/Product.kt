package ju.market.app.entity

import jakarta.persistence.*
import ju.market.system.enumm.UnitMeasure
import java.math.BigDecimal


//Tabela dos produtos que será criada no banco de dados
@Entity
@Table(name = "produtos")
data class Product(
    //ID será gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 30) val name: String,
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val unitOfMeasure: UnitMeasure,
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)") val unitPrice: BigDecimal,
    @Column(nullable = false) val quatity: Int,
    //Não estava na imagem do desafio, mas eu acredito que é necessário atribuir uma categoria para o produto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category = Category()
)
