package ju.market.app.service.impl

import ju.market.app.entity.Cart
import ju.market.app.repository.CartRepository
import ju.market.app.repository.ProductRepository
import ju.market.app.service.ICartService
import org.springframework.stereotype.Service


//Regras de negócio para o carrinho
@Service
class CartService (private val cartRepository: CartRepository
, private val productRepository: ProductRepository): ICartService {

    //Salva um novo produto no carrinho de acordo com as regras
    override fun saveCart(cart: Cart): String {
        //Calcula o preço total dos produtos
        cart.productPrice = this.productRepository.calculatePrice(cart.idProduct, cart.productName, cart.productQuatity)

        //Verifica a quantidade atual do produto em questão
        val atualQuatity: Int = this.productRepository.checkQntProduct(cart.idProduct)

        //Verifica se a quantidade de produtos do carrinho é menor ou igual ao do estoque do produto
        return if (cart.productQuatity <= atualQuatity) {
            //Verifica se o produto já existe no carrinho antes de adicionar
            val exist: Long? = this.cartRepository.existsCart(cart.idProduct)
            //Se existir, retorna a mensagem
            if (exist != null) {
                "Produto já está no carrinho."
            }else{
                //Salva o produto no carrinho
                this.cartRepository.save(cart)
                "O carrinho foi atualizado."
            }
        }else{
            //Retorna essa mensagem se o estoque for insuficiente
            "Não há estoque o suficiente para a quantidade solicitada."
        }

    }

    //Altera um produto do carrinho
    override fun updateCart(cart: Cart) {
        cart.productPrice = this.productRepository.calculatePrice(cart.idProduct, cart.productName, cart.productQuatity)
        this.cartRepository.update(cart.productName, cart.productQuatity, cart.productPrice)
    }

    //Retorna a lista dos produtos que estão no carrinho
    override fun findCart(): List<Cart> {
        return this.cartRepository.findAllProduct()
    }

    //Delete um produto do carrinho pelo ID do carrinho
    override fun deleteById(id: Long) {
        this.cartRepository.deleteById(id)
    }

    //Deleta o carrinho inteiro
    override fun deleteAll(){
        this.cartRepository.deleteAll()
    }

    //TRUNCATE na tabela, reseta os IDs e apaga a tabela
    override fun truncateAll(){
        this.cartRepository.truncateAll()
    }
}