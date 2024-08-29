package br.com.henrique.JWT.resources;

import br.com.henrique.JWT.models.dto.*;
import br.com.henrique.JWT.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos")
@RestController
@RequestMapping("/api/orders/v1")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Busca todos endereços cadastrados.")
    @GetMapping("/user/{id}")
    public List<OrderDto> findByUser(@PathVariable Long id){
        return orderService.findByUser(id);
    }

    @Operation(summary = "Busca um pedido pelo ID.")
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @Operation(summary = "Busca um pedido pelo ID e caso exista atualiza o status")
    @PutMapping("/status/{id}")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestBody OrderStatusDto orderStatus) {
        return ResponseEntity.ok(orderService.updateStatus(id, orderStatus));
    }

    @Operation(summary = "Busca um pedido pelo ID e caso exista atualiza o endereço")
    @PutMapping("/address/{id}")
    public ResponseEntity<OrderDto> updateAddress(@PathVariable Long id, @RequestBody OrderAddressDto orderAddressDto) {
        return ResponseEntity.ok(orderService.updateAddress(id, orderAddressDto));
    }

    @Operation(summary = "Cria um novo pedido.")
    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody OrderWithUserAddressDto orderWithUserDto){
        PaymentDto savedPayment = orderService.save(orderWithUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
    }

}