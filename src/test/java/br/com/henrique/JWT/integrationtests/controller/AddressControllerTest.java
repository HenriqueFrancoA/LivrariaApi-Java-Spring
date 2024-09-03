package br.com.henrique.JWT.integrationtests.controller;

import br.com.henrique.JWT.configs.TestConfigs;
import br.com.henrique.JWT.integrationtests.models.dto.AddressDto;
import br.com.henrique.JWT.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressControllerTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static AddressWithUserDto addressDto;

    @BeforeAll
    public static void setup(){
        objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        addressDto = new AddressWithUserDto();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {

        mockAddress();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZW5yaXF1ZSIsInJvbGVzIjpbIkFETUlOIl0sImV4cCI6MTcyNTMwODY2OCwiaWF0IjoxNzI1MzA1MDY4fQ.TNvJwrfEudxrjhz4-wUgbccvpLUeCAX5NfQN5NVvrwU")
                .setBasePath("/api/address/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        String content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)

                    .body(addressDto)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        AddressDto createdAddress = objectMapper.readValue(content, AddressDto.class);

        assertNotNull(createdAddress);
        assertNotNull(createdAddress.getId());
        assertNotNull(createdAddress.getCity());
        assertNotNull(createdAddress.getCountry());
        assertNotNull(createdAddress.getNumber());
        assertNotNull(createdAddress.getPublicPlace());
        assertNotNull(createdAddress.getNeighborhood());
        assertNotNull(createdAddress.getState());
        assertNotNull(createdAddress.getZipCode());

        assertTrue(createdAddress.getId() > 0);

        assertEquals("Brasil", createdAddress.getCountry());
        assertEquals("São Paulo", createdAddress.getCity());
        assertEquals("São Paulo", createdAddress.getState());
        assertEquals("Vila Guilherme", createdAddress.getNeighborhood());
        assertEquals("Rua Eugênio de Freitas", createdAddress.getPublicPlace());
        assertEquals("669", createdAddress.getNumber());
        assertEquals("02063-000", createdAddress.getZipCode());
    }

    private void mockAddress() {
        addressDto.setCountry("Brasil");
        addressDto.setUserId(2L);
        addressDto.setCity("São Paulo");
        addressDto.setState("São Paulo");
        addressDto.setNeighborhood("Vila Guilherme");
        addressDto.setPublicPlace("Rua Eugênio de Freitas");
        addressDto.setNumber("669");
        addressDto.setZipCode("02063-000");
    }
}
