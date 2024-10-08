package br.com.henrique.JWT.resources;

import br.com.henrique.JWT.models.dto.ItemOrderDto;
import br.com.henrique.JWT.models.dto.ItemOrderListDto;
import br.com.henrique.JWT.models.dto.ItemOrderQuantityDto;
import br.com.henrique.JWT.services.ItemOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Itens Pedidos")
@RestController
@RequestMapping("/api/items/v1")
public class ItemOrderResource  {

    @Autowired
    private ItemOrderService itemOrderService;

    @Operation(summary = "Adiciona novo(os) item(ns) ao pedido.")
    @PostMapping("/add")
    public ResponseEntity<List<ItemOrderDto>> addItem(@RequestBody ItemOrderListDto itemOrderListDto){
        List<ItemOrderDto> savedItemOrderDto = itemOrderService.addItem(itemOrderListDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItemOrderDto);
    }

    @Operation(summary = "Busca um item pelo ID e caso exista atualiza o mesmo.")
    @PutMapping("/{id}")
    public ResponseEntity<ItemOrderDto> update(@PathVariable Long id, @RequestBody ItemOrderQuantityDto itemOrderQuantityDto){
        return ResponseEntity.ok(itemOrderService.update(id, itemOrderQuantityDto));
    }

    @Operation(summary = "Remove um item do pedido.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        itemOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca um item pelo ID.")
    @GetMapping("{id}")
    public ResponseEntity<ItemOrderDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(itemOrderService.findById(id));
    }

}
