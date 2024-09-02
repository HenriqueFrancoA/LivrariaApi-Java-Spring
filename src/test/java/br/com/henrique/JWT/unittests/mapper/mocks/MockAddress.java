package br.com.henrique.JWT.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.henrique.JWT.models.Address;
import br.com.henrique.JWT.models.dto.AddressDto;

public class MockAddress {


    public Address mockEntity() {
        return mockEntity(0);
    }
    
    public AddressDto mockDto() {
        return mockDto(0);
    }
    
    public List<Address> mockEntityList() {
        List<Address> addrs = new ArrayList<Address>();
        for (int i = 0; i < 14; i++) {
            addrs.add(mockEntity(i));
        }
        return addrs;
    }

    public List<AddressDto> mockDtoList() {
        List<AddressDto> addrs = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            addrs.add(mockDto(i));
        }
        return addrs;
    }
    
    public Address mockEntity(Integer number) {
        Address address = new Address();
        address.setId(number.longValue());
        address.setCity("City Test" + number);
        address.setCountry("Country Test" + number);
        address.setNeighborhood("Neighborhood Test" + number);
        address.setState("State Test" + number);
        address.setZipCode("ZipCode Test" + number);
        address.setPublicPlace("Public Place Test" + number);
        address.setNumber("Number Test" + number);
        return address;
    }

    public AddressDto mockDto(Integer number) {
        AddressDto address = new AddressDto();
        address.setKey(number.longValue());
        address.setCity("City Test" + number);
        address.setCountry("Country Test" + number);
        address.setNeighborhood("Neighborhood Test" + number);
        address.setState("State Test" + number);
        address.setZipCode("ZipCode Test" + number);
        address.setPublicPlace("Public Place Test" + number);
        address.setNumber("Number Test" + number);
        return address;
    }

}
