package ju.market.app.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import ju.market.app.entity.Category
import ju.market.app.entity.Product
import ju.market.app.repository.CategoryRepository
import ju.market.app.repository.ProductRepository
import ju.market.app.service.impl.ProductService
import ju.market.system.enumm.UnitMeasure
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProductServiceTest {
    @MockK lateinit var productRepository: ProductRepository
    @InjectMockKs lateinit var productService: ProductService

    @MockK lateinit var categoryRepository: CategoryRepository

    //Método para testar se o produto é salvo quando a categoria existe
    @Test
    fun `should save product when category exists`() {
        // given
        val fakeCategory: Category = buildCategory()
        val fakeProduct: Product = buildProduct()

        every { categoryRepository.existsById(fakeCategory.id!!) } returns true
        every { productRepository.save(fakeProduct) } returns fakeProduct

        // when
        val actual: Product = productService.save(fakeProduct)

        // then
        assertThat(actual).isEqualTo(fakeProduct)
        verify(exactly = 1) { categoryRepository.existsById(fakeCategory.id!!) }
        verify(exactly = 1) { productRepository.save(fakeProduct) }
    }

    //Método para jogar uma exceção se a categoria não existir ao salvar o produto
    @Test
    fun `should throw IllegalArgumentException when category does not exist`() {
        // given
        val fakeCategory: Category = buildCategory()
        val fakeProduct: Product = buildProduct()

        every { categoryRepository.existsById(fakeCategory.id!!) } returns false

        // when/then
        assertThatThrownBy { productService.save(fakeProduct) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Categoria não existe.")
        verify(exactly = 1) { categoryRepository.existsById(fakeCategory.id!!) }
    }

    //Método para encontrar todos os produtos pela categoria
    @Test
    fun `should find all product by category`() {
        //given
        val fakeCategory: Category = buildCategory()
        val fakeProduct1: Product = buildProduct()
        val fakeProduct2: Product = buildProduct(id = 2L, name = "X-burguer")
        val fakeProductList: List<Product> = listOf(fakeProduct1, fakeProduct2)
        every { productRepository.findAllByCategory(fakeCategory.id!!) } returns fakeProductList

        //when
        val actual: List<Product> = productService.findAllByCategory(fakeCategory.id!!)

        //then
        assertThat(actual).hasSize(2)
        assertThat(actual).containsExactlyInAnyOrder(fakeProduct1, fakeProduct2)
        verify(exactly = 1) { productRepository.findAllByCategory(fakeCategory.id!!) }
    }

    //Método para encontrar um produto pelo id
    @Test
    fun `should find produtos by id`() {
        //given
        val fakeCategory: Category = buildCategory(name = "fakeCategory")
        val fakeProduct: Product = buildProduct(name = "fakeProduct", category = fakeCategory)
        every { productRepository.findById(fakeProduct.id!!) } returns Optional.of(fakeProduct)

        //when
        val actual: Optional<Product> = productService.findById(fakeProduct.id!!)

        //then
        assertThat(actual).isNotEmpty
        assertThat(actual.get()).isEqualTo(fakeProduct)
        verify(exactly = 1) { productRepository.findById(fakeProduct.id!!) }
    }

    //Método para não encontrar um produto se um ID for inválido
    @Test
    fun `should not find product by invalid id`() {
        //given
        val fakeId = 50L
        every { productRepository.findById(fakeId) } returns Optional.empty()

        //when
        val actual: Optional<Product> = productService.findById(fakeId)

        //then
        assertThat(actual).isEmpty
        verify(exactly = 1) { productRepository.findById(fakeId) }
    }

    //Método pra encontrar todos os produtos, sem condição
    @Test
    fun `should return all product`() {
        //given
        val fakeCategory: Category = buildCategory()
        val fakeProductList: List<Product> = listOf(
            buildProduct(),
            buildProduct(id = 2L, name = "X-burguer", category = fakeCategory),
            buildProduct(id = 3L, name = "X-tudo", category = fakeCategory)
        )
        every { productRepository.findAll() } returns fakeProductList

        //when
        val result: List<Product> = productService.findAllProduct()

        //then
        assertThat(result).isEqualTo(fakeProductList)
        verify(exactly = 1) { productRepository.findAll() }
    }

    //Método para mostrar uma lista vazia se nenhum produto existir
    @Test
    fun `should find empty list when no produtos exist`() {
        //given
        val fakeProductList: List<Product> = emptyList()
        every { productRepository.findAll() } returns fakeProductList

        //when
        val actual: List<Product> = productService.findAllProduct()

        //then
        assertThat(actual).isEmpty()
        verify(exactly = 1) { productRepository.findAll() }
    }

    //Método para encontrar produtos pelo nome
    @Test
    fun `should find product by name`() {
        //given
        val productFind = "sal"
        val fakeProduct1: Product = buildProduct()
        every { productRepository.findAllByName(productFind) } returns listOf(fakeProduct1)

        //when
        val actual: List<Product> = productService.findAllProductByName(productFind)

        //then
        assertThat(actual).isNotEmpty
        assertThat(actual).hasSize(1)
        assertThat(actual).containsExactly(fakeProduct1)
        verify(exactly = 1) { productRepository.findAllByName(productFind) }
    }

    //Método para alterar produtos
    @Test
    fun `should edit produtos`() {
        //given
        val fakeProduct: Product = buildProduct(name = "X-tudo", quatity = 17)
        every {
            productRepository.editProduct(
                fakeProduct.id!!,
                fakeProduct.name,
                fakeProduct.unitOfMeasure.name,
                fakeProduct.unitPrice,
                fakeProduct.category.id!!,
                fakeProduct.quatity
            )
        } returns 1

        //when
        val actual: String = productService.editProduct(fakeProduct)

        //then
        val message = "O produto de ID: ${fakeProduct.id} foi alterado com sucesso!"
        assertThat(actual).isEqualTo(message)
    }

    //Método para retornar que o produto não foi encontrado para ser alterado
    @Test
    fun `should return when product is not found`() {
        //given
        val fakeProduct: Product = buildProduct()
        every { productRepository.editProduct(any(), any(), any(), any(), any(), any()) } returns 0

        //when
        val actual: String = productService.editProduct(fakeProduct)

        //then
        assertEquals("Produto não encontrado.", actual)
        verify(exactly = 1) { productRepository.editProduct(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should delete produtos by Id`() {
        //given
        val fakeProduct: Product = buildProduct()
        every { productRepository.deleteById(fakeProduct.id!!) } just runs

        //when
        productService.delete(fakeProduct.id!!)

        //then
        verify(exactly = 1) { productRepository.deleteById(fakeProduct.id!!) }
    }

    //Método privado para criar produtos que seram usados para o teste
    private fun buildProduct(
        id: Long = 1L,
        name: String = "X-salada",
        unitPrice: BigDecimal = BigDecimal(6.50),
        quatity: Int = 7,
        unitOfMeasure: UnitMeasure = UnitMeasure.UNIT,
        category: Category = buildCategory()
    ) = Product(
        id = id,
        name = name,
        unitPrice = unitPrice,
        quatity = quatity,
        unitOfMeasure = unitOfMeasure,
        category = category
    )

    //Método privado para criar categorias que seram usados para o teste
    private fun buildCategory(
        id: Long = 1L,
        name: String = "Lanches"
    ) = Category(
        id = id,
        name = name
    )


}