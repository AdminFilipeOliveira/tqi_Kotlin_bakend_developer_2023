package ju.market.app.service.impl

import ju.market.app.entity.Cart
import ju.market.app.entity.Order
import ju.market.app.enumm.PaymentForm
import ju.market.app.repository.OrderRepository
import ju.market.app.repository.ProductRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

//Regras de negócio para os pedidos
@Service
class OrderService(private val cartService: CartService, private val productRepository: ProductRepository, private val orderRepository: OrderRepository):IOrderService {

    //Finalizar o pedido e retorna o Array com o valor total e código de venda
    override fun finalOrder(payment: PaymentForm): Array<Any>{
        //Lista o carrinho e guarda na variável
        val cart: List<Cart> = this.cartService.findCart()

        //Valor total iniciado por 0
        var totalValue: BigDecimal = BigDecimal.ZERO

        //Código de venda fixo para esse pedido
        val vendingCoce: UUID = UUID.randomUUID()

        //Uma iteração para cada item da lista de carrinhos
        for (item: Cart in cart) {

            //Cria o pedido com cada item
            val order = Order(
                productName = item.productName,
                idProduct = item.idProduct,
                unitPrice = item.productPrice,
                quatity = item.productQuatity,
                paymentForm = payment,
                vendingCode = vendingCoce.toString()
            )

            //Adiciona o valor de cada item do carrinho no valor total
            totalValue += item.productPrice

            //Remove o estoque do produto a cada iteração
            this.productRepository.removeProductStock(item.idProduct, item.productQuatity)

            //Salva cada pedido a cada iteração
            this.orderRepository.save(order)
        }
        //Faz o TRUNCATE na tabela e apaga de vez o carrinho
        this.cartService.truncateAll()
        //Retorna o array com o valor total e código de venda para a camada de controle
        return arrayOf(totalValue, vendingCoce.toString())
    }

    //Retorna a mensagem de acordo com o método de pagamento escolhido
    override fun paymentMethod(payment: PaymentForm): String{
        return when (payment){
            PaymentForm.CREDIT_CARD -> "Pagamento realizado com cartão de crédito."
            PaymentForm.DEBIT_CARD -> "Pagamento realizado com cartão de débito."
            PaymentForm.MONEY -> "Pagamento realizado em dinheiro."
            PaymentForm.PIX -> "Pagamento realizado via PIX."
        }
    }

    //Retorna todos os pedidos pelo código de venda
    override fun findByVendingCode(code: String): List<Order> {
        return this.orderRepository.findByVendingCode(code)
    }

    //Retorna todos os pedidos
    override fun findAll(): List<Order>{
        return this.orderRepository.findAll()
    }

    //Retorna o valor total dos pedidos pelo código de venda
    override fun findTotalByVendingCode(code: String): BigDecimal {
        return this.orderRepository.findTotalByVendingCode(code)
    }
}