package br.com.henrique.JWT.resources;

import br.com.henrique.JWT.models.dto.OrderDto;
import br.com.henrique.JWT.models.dto.OrderStatusDto;
import br.com.henrique.JWT.models.dto.OrderWithUserDto;
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
    @GetMapping
    public List<OrderDto> findAll(){
        return orderService.findAll();
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

    @Operation(summary = "Cria um novo pedido.")
    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderWithUserDto orderWithUserDto){
        OrderDto savedOrder = orderService.save(orderWithUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

}