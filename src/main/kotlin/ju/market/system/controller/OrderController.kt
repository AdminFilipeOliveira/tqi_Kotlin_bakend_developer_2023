package ju.market.app.controller

import ju.market.app.dto.FinalVendingDto
import ju.market.app.entity.Order
import ju.market.app.finalBay.FinalVendingResponse
import ju.market.app.service.impl.OrderService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/pedidos")
class OrderController(private val ordersService: OrderService) {

    //Checkout, finaliza a venda
    @PostMapping()
    fun finalVending(@RequestBody @Valid finalVendingDto: FinalVendingDto): ResponseEntity<FinalVendingResponse> {

        //Recebe um array que contém o código de venda e o valor total
        val value: Array<Any> = this.ordersService.finalOrder(finalVendingDto.paymentForm)
        //Recebe a mensagem que será retornada para o cliente sobre a forma de pagamento escolhida
        val paymentForm: String = this.ordersService.paymentMethod(finalVendingDto.paymentForm)

        //Instrução de como proceder ao fazer a compra
        val mensage: String = "Obrigado por comprar na JuMarket! Volte sempre!!! Leve o papel que contém o código de venda para obter seus produtos no balcão!"

        //Formatar o valor total para reais
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val formatTotalValue: String = numberFormat.format(value[0])

        //Resposta retornada contendo o valor total, mensagem da forma de pagamento, código de venda e instrução
        val response = FinalVendingResponse(paymentForm, formatTotalValue, value[1].toString(), mensage)
        return ResponseEntity.ok(response)
    }

    //Retorna todos os pedidos pelo código de venda
    @GetMapping()
    fun findByVendingCode(@RequestParam(required = false, defaultValue = "") code: String,
                       @RequestParam(required = false, defaultValue = "false") total: Boolean): ResponseEntity<out Any> {

        //Se o código de venda não for vazio.
        if(code.isNotEmpty()){
            //Verifica se o parâmetro 'total' é falso
            if(!total) {
                //Retorna todos os pedidos pelo código de venda se o 'total' for falso
                val sale: List<Order> = this.ordersService.findByVendingCode(code)
                return ResponseEntity.ok().body(sale)
            }else{
                //Se o total for verdadeiro, retorna somente o valor total dos pedidos pelo código de venda, formatado
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                val sale: BigDecimal = this.ordersService.findTotalByVendingCode(code)
                return ResponseEntity.ok().body(numberFormat.format(sale))
            }
        }else{
            //Se nenhuma condição for verdadeira, apenas retorna todos os pedidos com todos os códigos de venda
            val sale: List<Order> = this.ordersService.findAll()
            return ResponseEntity.ok().body(sale)
        }
    }
}