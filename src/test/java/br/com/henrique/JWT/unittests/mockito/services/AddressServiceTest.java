package br.com.henrique.JWT.unittests.mockito.services;

import br.com.henrique.JWT.models.Address;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.dto.AddressDto;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import br.com.henrique.JWT.models.dto.AddressWithoutUserDto;
import br.com.henrique.JWT.repositorys.AddressRepository;
import br.com.henrique.JWT.repositorys.UserRepository;
import br.com.henrique.JWT.services.AddressService;
import br.com.henrique.JWT.unittests.mapper.mocks.MockAddress;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    MockAddress input;

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception{
        input = new MockAddress();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById(){
        Address address = input.mockEntity(1);
        address.setId(1L);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        AddressDto result = addressService.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        System.out.println(result.toString());

        assertEquals("City Test1", result.getCity());
        assertEquals("Country Test1", result.getCountry());
        assertEquals("Neighborhood Test1", result.getNeighborhood());
        assertEquals("State Test1", result.getState());
        assertEquals("ZipCode Test1", result.getZipCode());
        assertEquals("Public Place Test1", result.getPublicPlace());
        assertEquals("Number Test1", result.getNumber());

    }

    @Test
    void testCreate(){
        Address address = input.mockEntity(1);

        Address persisted = address;
        persisted.setId(1L);

        AddressDto addressDto = input.mockDto(1);
        addressDto.setKey(1L);

        when(addressRepository.save(any(Address.class))).thenReturn(persisted);
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));

        AddressWithUserDto addressWithUserDto = new AddressWithUserDto(
                addressDto.getKey(),
                2L,
                addressDto.getPublicPlace(),
                addressDto.getNumber(),
                addressDto.getNeighborhood(),
                addressDto.getCity(),
                addressDto.getState(),
                addressDto.getZipCode(),
                addressDto.getCountry()
        );

        AddressDto result = addressService.save(addressWithUserDto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/address/v1/1>;rel=\"self\"]"));

        assertEquals("City Test1", result.getCity());
        assertEquals("Country Test1", result.getCountry());
        assertEquals("Neighborhood Test1", result.getNeighborhood());
        assertEquals("State Test1", result.getState());
        assertEquals("ZipCode Test1", result.getZipCode());
        assertEquals("Public Place Test1", result.getPublicPlace());
        assertEquals("Number Test1", result.getNumber());
    }

    @Test
    void testUpdate(){
        Address address = input.mockEntity(5);
        address.setId(5L);

        Address persisted = address;
        persisted.setId(5L);

        AddressDto addressDto = input.mockDto(5);
        addressDto.setKey(5L);

        when(addressRepository.findById(5L)).thenReturn(Optional.of(new Address()));
        when(addressRepository.save(any(Address.class))).thenReturn(persisted);

        AddressWithoutUserDto addressWithoutUserDto = new AddressWithoutUserDto(
                addressDto.getPublicPlace(),
                addressDto.getNumber(),
                addressDto.getNeighborhood(),
                addressDto.getCity(),
                addressDto.getState(),
                addressDto.getZipCode(),
                addressDto.getCountry()
        );

        AddressDto result = addressService.update(5L, addressWithoutUserDto);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/address/v1/5>;rel=\"self\"]"));

        assertEquals("City Test5", result.getCity());
        assertEquals("Country Test5", result.getCountry());
        assertEquals("Neighborhood Test5", result.getNeighborhood());
        assertEquals("State Test5", result.getState());
        assertEquals("ZipCode Test5", result.getZipCode());
        assertEquals("Public Place Test5", result.getPublicPlace());
        assertEquals("Number Test5", result.getNumber());
    }

    @Test
    void testDelete(){
        Address address = input.mockEntity(5);
        address.setId(5L);

        when(addressRepository.existsById(5L)).thenReturn(true);

        addressService.delete(5L);
    }

    @Test
    void testCreateExceptions(){
        AddressWithUserDto addressWithUserDto = new AddressWithUserDto(
                1L,
                7L,
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        Exception exception = assertThrows(EntityNotFoundException.class, () ->{
            addressService.save(addressWithUserDto);
        });

        String expectedMessage = "Usuário não encontrado.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testFindAll(){
        List<Address> listAddress = input.mockEntityList();

        when(addressRepository.findAll()).thenReturn(listAddress);

        List<AddressDto> result = addressService.findAll();

        assertNotNull(result);
        assertEquals(14, result.size());

        AddressDto addressOne = result.get(1);

        assertNotNull(addressOne);
        assertNotNull(addressOne.getKey());
        assertNotNull(addressOne.getLinks());

        assertTrue(addressOne.toString().contains("links: [</api/address/v1/1>;rel=\"self\"]"));

        assertEquals("City Test1", addressOne.getCity());
        assertEquals("Country Test1", addressOne.getCountry());
        assertEquals("Neighborhood Test1", addressOne.getNeighborhood());
        assertEquals("State Test1", addressOne.getState());
        assertEquals("ZipCode Test1", addressOne.getZipCode());
        assertEquals("Public Place Test1", addressOne.getPublicPlace());
        assertEquals("Number Test1", addressOne.getNumber());

        AddressDto addressFive = result.get(5);

        assertNotNull(addressFive);
        assertNotNull(addressFive.getKey());
        assertNotNull(addressFive.getLinks());

        assertTrue(addressFive.toString().contains("links: [</api/address/v1/5>;rel=\"self\"]"));

        assertEquals("City Test5", addressFive.getCity());
        assertEquals("Country Test5", addressFive.getCountry());
        assertEquals("Neighborhood Test5", addressFive.getNeighborhood());
        assertEquals("State Test5", addressFive.getState());
        assertEquals("ZipCode Test5", addressFive.getZipCode());
        assertEquals("Public Place Test5", addressFive.getPublicPlace());
        assertEquals("Number Test5", addressFive.getNumber());
    }
}
