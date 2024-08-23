package br.com.henrique.JWT.resources;

import br.com.henrique.JWT.models.dto.AddressDto;
import br.com.henrique.JWT.models.dto.AddressWithoutUserDto;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import br.com.henrique.JWT.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endereços")
@RestController
@RequestMapping("/api/address/v1")
public class AddressResource {

    @Autowired
    private AddressService addressService;

    @Operation(summary = "Busca todos endereços cadastrados.")
    @GetMapping
    public List<AddressDto> findAll(){
        return addressService.findAll();
    }

    @Operation(summary = "Busca um endereço pelo ID.")
    @GetMapping("{id}")
    public ResponseEntity<AddressDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(addressService.findById(id));
    }

    @Operation(summary = "Busca um endereço pelo ID e caso exista atualiza o mesmo")
    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody AddressWithoutUserDto addressWithoutUserDto) {
        return ResponseEntity.ok(addressService.update(id, addressWithoutUserDto));
    }

    @Operation(summary = "Cria um novo endreço.")
    @PostMapping
    public ResponseEntity<AddressDto> create(@RequestBody AddressWithUserDto addressWithUserDto) {
        AddressDto savedAddress = addressService.save(addressWithUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    @Operation(summary = "Deleta o endereço pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
