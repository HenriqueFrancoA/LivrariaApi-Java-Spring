package br.com.henrique.JWT.services;

import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Address;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.dto.AddressDto;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import br.com.henrique.JWT.models.dto.AddressWithoutUserDto;
import br.com.henrique.JWT.repositorys.AddressRepository;
import br.com.henrique.JWT.repositorys.UserRepository;
import br.com.henrique.JWT.resources.AddressResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AddressService  {

    private final Logger logger = Logger.getLogger(AddressService.class.getName());

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PagedResourcesAssembler<AddressDto> assembler;

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

        AddressDto addressDto =  DozerMapper.parseObject( addressRepository.save(adr), AddressDto.class);

        addressDto.add(linkTo(methodOn(AddressResource.class).findById(addressDto.getKey())).withSelfRel());

        return addressDto;
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

    public PagedModel<EntityModel<AddressDto>> findAll(Pageable pageable) {
        logger.info("Retornando todos endereços...");

        Page<Address> addressPage = addressRepository.findAll(pageable);

        Page<AddressDto> addressDtoPage = addressPage.map(a -> DozerMapper.parseObject(a, AddressDto.class));

        addressDtoPage.map(a -> a.add(linkTo(methodOn(AddressResource.class).findById(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AddressResource.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(addressDtoPage, link);
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
