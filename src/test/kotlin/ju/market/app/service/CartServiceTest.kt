package ju.market.app.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import ju.market.app.entity.Cart
import ju.market.app.entity.Category
import ju.market.app.repository.CartRepository
import ju.market.app.repository.ProductRepository
import ju.market.app.service.impl.CartService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals


import java.math.BigDecimal

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)

class CartServiceTest {
    @MockK lateinit var cartRepository: CartRepository
    @MockK lateinit var productRepository: ProductRepository
    @InjectMockKs lateinit var cartService: CartService

    //Produto salvo no carrinho
    @Test
    fun `should return 'O cart foi atualizado com sucesso' when produto is added to carrinho`()  {

        val fakeCart: Cart = buildCart()
        every { productRepository.calculatePrice(fakeCart.idProduct, fakeCart.productName, fakeCart.productQuatity) } returns BigDecimal(19.50)
        every { productRepository.checkQntProduct(any()) } returns 5
        every { cartRepository.existsCart(any()) } returns null
        every { cartRepository.save(any()) } returns fakeCart


        val actual: String = cartService.saveCart(fakeCart)


        assertEquals("O carrinho foi atualizado.", actual)
        verify(exactly = 1) { productRepository.calculatePrice(any(), any(), any()) }
        verify(exactly = 1) { productRepository.checkQntProduct(any()) }
        verify(exactly = 1) { cartRepository.existsCart(any()) }
        verify(exactly = 1) { cartRepository.save(any()) }
    }

    // Retorna o produto já existe no carrinho
    @Test
    fun `should return 'Produto já está no carrinho' when produto is already in carrinho`() {

        val fakeCart: Cart = buildCart()
        every { productRepository.calculatePrice(fakeCart.idProduct, fakeCart.productName, fakeCart.productQuatity) } returns BigDecimal(19.50)
        every { productRepository.checkQntProduct(any()) } returns 10
        every { cartRepository.existsCart(any()) } returns fakeCart.idProduct


        val actual: String = cartService.saveCart(fakeCart)


        assertEquals("Produto já está no carrinho.", actual)
        verify(exactly = 1) { productRepository.calculatePrice(any(), any(), any()) }
        verify(exactly = 1) { productRepository.checkQntProduct(any()) }
        verify(exactly = 1) { cartRepository.existsCart(any()) }
    }

    // Retornar quando não há stock
    @Test
    fun `should return 'Não há stock o suficiente para a quantidade solicitada' when produto has insufficient stock`() {

        val fakeCart: Cart = buildCart()
        every { productRepository.calculatePrice(fakeCart.idProduct, fakeCart.productName, fakeCart.productQuatity) } returns BigDecimal(19.50)
        every { productRepository.checkQntProduct(any()) } returns 2


        val actual: String = cartService.saveCart(fakeCart)


        assertEquals("Não há estoque o suficiente para a quantidade solicitada.", actual)
        verify(exactly = 1) { productRepository.calculatePrice(any(), any(), any()) }
        verify(exactly = 1) { productRepository.checkQntProduct(any()) }
    }

    // Atualiza um produto no carrinho
    @Test
    fun `should update carrinho correctly`() {

        val fakeCart: Cart = buildCart()
        val calculatePrice: BigDecimal = BigDecimal(3.5)
        every { productRepository.calculatePrice(fakeCart.idProduct, fakeCart.productName, fakeCart.productQuatity) } returns calculatePrice
        every { cartRepository.update(fakeCart.productName, fakeCart.productQuatity, calculatePrice) } just runs


        cartService.updateCart(fakeCart)


        verify(exactly = 1) { productRepository.calculatePrice(any(), any(), any()) }
        verify(exactly = 1) { cartRepository.update(fakeCart.productName, fakeCart.productQuatity, calculatePrice) }
    }

    // Retorna todos os produtos do carrinho
    @Test
    fun `should return all produtos no carrinho`() {

        val fakeCar: List<Cart> = listOf(
            buildCart(),
            buildCart(id = 1, productName = "Café", productQuatity = 2)
        )
        every { cartRepository.findAllProduct() } returns fakeCar

        val carts: List<Cart> = cartService.findCart()

        assertEquals(fakeCar, carts)
        verify(exactly = 1) { cartRepository.findAllProduct() }
    }

    // Deleta um produto do carrinho pelo ID
    @Test
    fun `should delete carrinho by id`() {
        //given
        val fakeCart: Cart = buildCart()
        every { cartRepository.deleteById(fakeCart.id!!) } just runs

        //when
        cartService.deleteById(fakeCart.id!!)

        //then
        verify(exactly = 1) { cartRepository.deleteById(fakeCart.id!!) }
    }

    // Truncate de todos os carrinho
    @Test
    fun `should truncate all carrinhos`() {

        buildCart()
        every { cartRepository.truncateAll() } just runs


        cartService.truncateAll()

        verify(exactly = 1) { cartRepository.truncateAll() }
        confirmVerified(cartRepository)
    }

    // Cria um carrinho para o Test
    private fun buildCart(
        id: Long = 1L,
        idProduct: Long = 1L,
        productName: String = "Chocolate",
        productQuatity: Int = 5,
        productPrice: BigDecimal = BigDecimal(2.50)
    ) = Cart(
        id = id,
        idProduct = idProduct,
        productName = productName,
        productQuatity = productQuatity,
        productPrice = productPrice
    )

    // Criar categorias para o teste
    private fun buildCategory(
        id: Long = 1L,
        name: String = "Detergentes"
    ) = Category(
        id = id,
        name = name
    )


}


