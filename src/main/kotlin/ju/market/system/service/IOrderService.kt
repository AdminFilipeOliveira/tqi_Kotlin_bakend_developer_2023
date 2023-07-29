package ju.market.app.service.impl


import ju.market.app.entity.Order
import ju.market.app.enumm.PaymentForm

import java.math.BigDecimal



//Implementações
interface IOrderService {
    fun finalOrder(payment: PaymentForm): Array<Any>
    fun paymentMethod(payment: PaymentForm): String
    fun findByVendingCode(code: String): List<Order>
    fun findAll(): List<Order>
    fun findTotalByVendingCode(code: String): BigDecimal
}