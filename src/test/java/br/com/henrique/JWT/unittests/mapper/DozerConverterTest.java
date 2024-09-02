package br.com.henrique.JWT.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Address;
import br.com.henrique.JWT.models.dto.AddressDto;
import br.com.henrique.JWT.unittests.mapper.mocks.MockAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DozerConverterTest {
    
    MockAddress inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockAddress();
    }

    @Test
    public void parseEntityToDtoTest() {
        AddressDto output = DozerMapper.parseObject(inputObject.mockEntity(), AddressDto.class);
        assertEquals(0L, output.getKey());
        assertEquals("City Test0", output.getCity());
        assertEquals("Country Test0", output.getCountry());
        assertEquals("Neighborhood Test0", output.getNeighborhood());
        assertEquals("State Test0", output.getState());
        assertEquals("ZipCode Test0", output.getZipCode());
        assertEquals("Public Place Test0", output.getPublicPlace());
        assertEquals("Number Test0", output.getNumber());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<AddressDto> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), AddressDto.class);
        AddressDto outputZero = outputList.get(0);

        assertEquals(0L, outputZero.getKey());
        assertEquals("City Test0", outputZero.getCity());
        assertEquals("Country Test0", outputZero.getCountry());
        assertEquals("Neighborhood Test0", outputZero.getNeighborhood());
        assertEquals("State Test0", outputZero.getState());
        assertEquals("ZipCode Test0", outputZero.getZipCode());
        assertEquals("Public Place Test0", outputZero.getPublicPlace());
        assertEquals("Number Test0", outputZero.getNumber());

        AddressDto outputSeven = outputList.get(7);

        assertEquals(7L, outputSeven.getKey());
        assertEquals("City Test7", outputSeven.getCity());
        assertEquals("Country Test7", outputSeven.getCountry());
        assertEquals("Neighborhood Test7", outputSeven.getNeighborhood());
        assertEquals("State Test7", outputSeven.getState());
        assertEquals("ZipCode Test7", outputSeven.getZipCode());
        assertEquals("Public Place Test7", outputSeven.getPublicPlace());
        assertEquals("Number Test7", outputSeven.getNumber());

        AddressDto outputTwelve = outputList.get(12);

        assertEquals(12L, outputTwelve.getKey());
        assertEquals("City Test12", outputTwelve.getCity());
        assertEquals("Country Test12", outputTwelve.getCountry());
        assertEquals("Neighborhood Test12", outputTwelve.getNeighborhood());
        assertEquals("State Test12", outputTwelve.getState());
        assertEquals("ZipCode Test12", outputTwelve.getZipCode());
        assertEquals("Public Place Test12", outputTwelve.getPublicPlace());
        assertEquals("Number Test12", outputTwelve.getNumber());
    }

    @Test
    public void parseDtoToEntityTest() {
        Address output = DozerMapper.parseObject(inputObject.mockEntity(), Address.class);
        assertEquals(0L, output.getId());
        assertEquals("City Test0", output.getCity());
        assertEquals("Country Test0", output.getCountry());
        assertEquals("Neighborhood Test0", output.getNeighborhood());
        assertEquals("State Test0", output.getState());
        assertEquals("ZipCode Test0", output.getZipCode());
        assertEquals("Public Place Test0", output.getPublicPlace());
        assertEquals("Number Test0", output.getNumber());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Address> outputList = DozerMapper.parseListObjects(inputObject.mockDtoList(), Address.class);
        Address outputZero = outputList.get(0);

        assertEquals(0L, outputZero.getId());
        assertEquals("City Test0", outputZero.getCity());
        assertEquals("Country Test0", outputZero.getCountry());
        assertEquals("Neighborhood Test0", outputZero.getNeighborhood());
        assertEquals("State Test0", outputZero.getState());
        assertEquals("ZipCode Test0", outputZero.getZipCode());
        assertEquals("Public Place Test0", outputZero.getPublicPlace());
        assertEquals("Number Test0", outputZero.getNumber());
        
        Address outputSeven = outputList.get(7);

        assertEquals(7L, outputSeven.getId());
        assertEquals("City Test7", outputSeven.getCity());
        assertEquals("Country Test7", outputSeven.getCountry());
        assertEquals("Neighborhood Test7", outputSeven.getNeighborhood());
        assertEquals("State Test7", outputSeven.getState());
        assertEquals("ZipCode Test7", outputSeven.getZipCode());
        assertEquals("Public Place Test7", outputSeven.getPublicPlace());
        assertEquals("Number Test7", outputSeven.getNumber());
        
        Address outputTwelve = outputList.get(12);

        assertEquals(12L, outputTwelve.getId());
        assertEquals("City Test12", outputTwelve.getCity());
        assertEquals("Country Test12", outputTwelve.getCountry());
        assertEquals("Neighborhood Test12", outputTwelve.getNeighborhood());
        assertEquals("State Test12", outputTwelve.getState());
        assertEquals("ZipCode Test12", outputTwelve.getZipCode());
        assertEquals("Public Place Test12", outputTwelve.getPublicPlace());
        assertEquals("Number Test12", outputTwelve.getNumber());
    }
}
