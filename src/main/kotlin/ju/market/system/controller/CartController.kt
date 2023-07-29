package ju.market.app.controller

import ju.market.app.dto.CartDto
import ju.market.app.entity.Cart
import ju.market.app.service.impl.CartService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/carrinho")
class CartController(private val cartService: CartService) {

    //Salva um produto no carrinho
    @PostMapping()
    fun saveCart(@RequestBody @Valid cartDto: CartDto): ResponseEntity<String>{
        val response: String = this.cartService.saveCart(cartDto.toEntity())

        return if(response == "O carrinho foi atualizado!") ResponseEntity.status(HttpStatus.OK).body(response)
        else ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    //Retorna a lista de produtos no carrinho
    @GetMapping
    fun getCart(): ResponseEntity<List<Cart>>{
        val cart: List<Cart> = this.cartService.findCart()
        return ResponseEntity.status(HttpStatus.OK).body(cart)
    }

    //Altera os produtos do carrinho
    @PatchMapping("/update")
    fun updateCart(@RequestBody cartDto: CartDto): ResponseEntity<String>{
        this.cartService.updateCart(cartDto.toEntity())
        val response = "O carrinho foi alterado."
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Deleta um produto pelo ID do carrinho
    @DeleteMapping("/{id}")
    fun deleteCartById(@PathVariable id: Long){
        this.cartService.deleteById(id)
    }

    //Deleta o carrinho inteiro
    @DeleteMapping
    fun deleteCart(){
        this.cartService.deleteAll()
    }

}