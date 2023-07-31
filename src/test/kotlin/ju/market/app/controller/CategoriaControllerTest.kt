package ju.market.app.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import ju.market.app.dto.CategoryDto
import ju.market.app.entity.Category
import ju.market.app.repository.CategoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles("teste")
@AutoConfigureMockMvc
@ContextConfiguration
class CategoryControllerTest {

    //Anotação para injetar a dependências
    @Autowired private lateinit var categoryRepository: CategoryRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    //Endpoint principal da categoria
    companion object{
        const val URL: String = "/api/categoria"
    }

    //Deleta tudo antes de cada teste
    @BeforeEach fun setup(){
        categoryRepository.deleteAll()
    }

    //Deleta tudo após cada teste
    @AfterEach fun tearDown(){
        categoryRepository.deleteAll()
    }

    //POST, salva uma nova categoria
    @Test
    fun `should save a new category`() {
        // given
        val categoryDto: CategoryDto = buildCategoryDto()
        val valueAsString: String = objectMapper.writeValueAsString(categoryDto)

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val savedCategories: MutableList<Category> = categoryRepository.findAll()
        assertThat(savedCategories).hasSize(1)

        val saveCategory: Category = savedCategories[0]
        assertThat(saveCategory.name).isEqualTo(categoryDto.name)
    }

    //GET, retorna a categoria pelo id
    @Test
    fun `should get category by id`() {
        //given
        val savedCategory: Category = categoryRepository.save(Category(id = 1, name = "Teste"))

        //when
        val actual = mockMvc.perform(MockMvcRequestBuilders.get("$URL/${savedCategory.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        //then
        val responseJson: String = actual.response.contentAsString
        val categoryResponse: Category = objectMapper.readValue(responseJson, Category::class.java)
        assertThat(categoryResponse).isNotNull()
        assertThat(categoryResponse.id).isEqualTo(savedCategory.id)
        assertThat(categoryResponse.name).isEqualTo(savedCategory.name)
    }

    //GET, retorna todas as categorias
    @Test
    fun `should get all categories`() {
        // given
        val category1: Category = categoryRepository.save(Category(id = 1, name = "Teste1"))
        val category2: Category = categoryRepository.save(Category(id = 2, name = "Teste2"))

        // when
        val actual = mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // then
        val responseJson: String = actual.response.contentAsString
        val categoryResponseList = objectMapper.readValue<List<Category>>(responseJson, object : TypeReference<List<Category>>() {})
        assertThat(categoryResponseList).isNotEmpty()
        assertThat(categoryResponseList).hasSize(2)
        assertThat(categoryResponseList).contains(category1, category2)
    }

    //DELETE, delete uma categoria pelo id
    @Test
    fun `should delete category by id`() {
        // given
        val category = categoryRepository.save(Category(id = 2, name = "Teste"))

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${category.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        assertThat(categoryRepository.existsById(category.id!!)).isFalse()
    }

    //DELETE, deleta todas as categorias
    @Test
    fun `should delete all categories`() {
        // given
        categoryRepository.save(Category(id = 1, name = "Teste"))
        categoryRepository.save(Category(id = 2, name = "Teste2"))

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete(URL))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        assertThat(categoryRepository.count()).isEqualTo(0)
    }

    //Dto da categoria
    private fun buildCategoryDto(
        name: String = "Mercearia"
    ) = CategoryDto(
        name = name
    )

}