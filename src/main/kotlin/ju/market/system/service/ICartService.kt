package ju.market.app.service

import ju.market.app.entity.Cart


//Implementações
interface ICartService {
    fun saveCart(cart: Cart): String
    fun updateCart(cart: Cart)
    fun findCart(): List<Cart>
    fun deleteById(id: Long)
    fun deleteAll()
    fun truncateAll()
}