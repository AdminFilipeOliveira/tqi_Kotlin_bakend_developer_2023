package ju.market.app.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import ju.market.app.entity.Category
import ju.market.app.repository.CategoryRepository
import ju.market.app.service.impl.CategoryService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles

import org.assertj.core.api.Assertions.*
import java.lang.RuntimeException
import java.util.Optional
import java.util.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CategoryServiceTest {
    @MockK lateinit var categoryRepository: CategoryRepository
    @InjectMockKs lateinit var categoryService: CategoryService

    // Salva categoria
    @Test
    fun `should save category`(){

        val fakeCategory: Category = buildCategory()
        every { categoryRepository.save(any()) } returns fakeCategory

        val actual: Category = categoryService.save(fakeCategory)

        assertThat(actual).isNotNull
        assertThat(actual).isSameAs(fakeCategory)

        verify(exactly = 1){ categoryRepository.save(fakeCategory) }
    }

    @Test
    fun `should return all categoryes`() {

        val fakeCategory1 = Category()
        val fakeCategory2 = Category(id = 2, name = "Charcutaria")
        val fakeCategory3 = Category(id = 3, name = "Massas")
        val fakeCategorys: List<Category> = listOf(fakeCategory1, fakeCategory2, fakeCategory3)

        every { categoryRepository.findAll() } returns fakeCategorys


        val actual: List<Category> = categoryService.findAllCategory()


        assertThat(actual).isEqualTo(fakeCategorys)
    }

    // Chama a categoria pelo Id
    @Test
    fun `should return category by id`(){

        val fakeId: Long = Random().nextLong()
        val fakeCategory: Category = buildCategory(id = fakeId)
        every { categoryRepository.findById(fakeId) } returns Optional.of(fakeCategory)

        val actual: Category = categoryService.findById(fakeId)

        assertThat(actual).isExactlyInstanceOf(Category::class.java)
        assertThat(actual).isNotNull
        verify(exactly = 1){ categoryRepository.findById(fakeId) }
    }

    @Test
    fun `should not find category by invalid id and throw BusinessException`() {

        val fakeId: Long = Random().nextLong()
        every { categoryRepository.findById(fakeId) } returns Optional.empty()


        assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { categoryService.findById(fakeId) }
            .withMessage("Id $fakeId não encontrado.")
        verify(exactly = 1) { categoryRepository.findById(fakeId) }
    }

    // Deleta a category pelo id
    @Test
    fun `should delete categoria by id`() {

        val fakeId: Long = Random().nextLong()
        val fakeCategory: Category = buildCategory(id = fakeId)
        every { categoryRepository.findById(fakeId) } returns Optional.of(fakeCategory)
        every { categoryRepository.deleteById(fakeId) } just runs

        categoryService.deleteById(fakeId)

        verify(exactly = 1) { categoryRepository.deleteById(fakeId) }
    }

    @Test
    fun `should delete all categorys`() {

        val fakeCategory1: Category = buildCategory()
        val fakeCategory2: Category = buildCategory(id = 2, name = "Charcutaria")
        val fakeCategorys: List<Category> = listOf(fakeCategory1, fakeCategory2)
        every { categoryRepository.findAll() } returns fakeCategorys
        every { categoryRepository.deleteAll() } just runs


        categoryService.deleteAll()


        verify(exactly = 1) { categoryRepository.deleteAll() }
    }

    private fun buildCategory(
        id: Long = 1L,
        name: String = "Detergentes"
    ) = Category(
        id = id,
        name = name
    )

}