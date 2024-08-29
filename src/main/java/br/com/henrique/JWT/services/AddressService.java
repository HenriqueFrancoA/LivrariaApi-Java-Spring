package br.com.henrique.JWT.services;

import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Address;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.dto.AddressDto;
import br.com.henrique.JWT.models.dto.AddressWithoutUserDto;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import br.com.henrique.JWT.repositorys.AddressRepository;
import br.com.henrique.JWT.repositorys.UserRepository;
import br.com.henrique.JWT.resources.AddressResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.logging.Logger;

@Service
public class AddressService  {

    private final Logger logger = Logger.getLogger(AddressService.class.getName());

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public AddressDto update(Long id, AddressWithoutUserDto adressWithoutUserDto) {
        logger.info("Atualizando Endereço, ID: " + id);
        Address adr = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID do Endereço não encontrado."));

        adr.setPublicPlace(adressWithoutUserDto.getPublicPlace());
        adr.setNumber(adressWithoutUserDto.getNumber());
        adr.setNeighborhood(adressWithoutUserDto.getNeighborhood());
        adr.setCity(adressWithoutUserDto.getCity());
        adr.setState(adressWithoutUserDto.getState());
        adr.setZipCode(adressWithoutUserDto.getZipCode());
        adr.setCountry(adressWithoutUserDto.getCountry());

        return  DozerMapper.parseObject( addressRepository.save(adr), AddressDto.class) ;
    }

    public AddressDto save(AddressWithUserDto addressWithUserDto) {
        logger.info("Criando Endereço.");
        User user = userRepository.findById(addressWithUserDto.getUserId())
                .orElseThrow(() ->  new EntityNotFoundException("Usuário não encontrado."));

        Address address = new Address(
                addressWithUserDto,
                user
        );

        AddressDto addressDto = DozerMapper.parseObject( addressRepository.save(address), AddressDto.class);
        addressDto.add(linkTo(methodOn(AddressResource.class).findById(addressDto.getKey())).withSelfRel());

        return addressDto;
    }

    public List<AddressDto> findAll() {
        logger.info("Retornando todos endereços...");

        List<AddressDto> listAddress = DozerMapper.parseListObjects(addressRepository.findAll(), AddressDto.class);

        for (AddressDto a : listAddress) {
            a.add(linkTo(methodOn(AddressResource.class).findById(a.getKey())).withSelfRel());
        }

        return listAddress;
    }

    public AddressDto findById(Long id) {
        logger.info("Procurando Endereço do ID: " + id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Endereço não encontrado."));

        AddressDto addressDto = DozerMapper.parseObject(address, AddressDto.class);
        addressDto.add(linkTo(methodOn(AddressResource.class).findById(id)).withSelfRel());

        return addressDto;
    }

    public void delete(Long id) {
        logger.info("Deletando endereço do ID: " + id);
        if (!addressRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado.");
        }
        addressRepository.deleteById(id);
    }
}
