package br.com.henrique.JWT.integrationtests.controller;

import br.com.henrique.JWT.configs.TestConfigs;
import br.com.henrique.JWT.integrationtests.models.dto.AddressDto;
import br.com.henrique.JWT.integrationtests.models.dto.wrapper.WrapperAddressDto;
import br.com.henrique.JWT.integrationtests.models.vo.TokenVO;
import br.com.henrique.JWT.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.henrique.JWT.models.Permission;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.dto.AddressWithUserDto;
import br.com.henrique.JWT.models.vo.AccountCredentialsVO;
import br.com.henrique.JWT.repositories.PermissionRepository;
import br.com.henrique.JWT.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressControllerTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static AddressWithUserDto addressDto;
    private static Long createdUserId;

    @BeforeAll
    public static void setup(){
        objectMapper = new ObjectMapper();

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        addressDto = new AddressWithUserDto();
    }

    @Test
    @Order(0)
    public void setupTestUserAndPermissions() {
        Permission permission = new Permission();
        permission.setDescription("ROLE_USER");
        permissionRepository.save(permission);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("",
        8, 185000,
        SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        encoders.put("pbkdf2", pbkdf2PasswordEncoder);
        DelegatingPasswordEncoder passwordEncoder = new
        DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2PasswordEncoder);

        String password = passwordEncoder.encode("123456");

        User user = new User(
                "testuser",
                "Test User",
                password,
                true,
                true,
                true,
                true
        );

        User userCreated = userRepository.save(user);

        createdUserId = 5L;
    }

    @Test
    @Order(1)
    public void testAuthentication() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("henrique", "123456");

        String accessToken = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                    .then()
                        .statusCode(200)
                            .extract()
                            .body()
                                .as(TokenVO.class)
                            .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/address/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(2)
    public void testCreateAddress() throws JsonProcessingException {
        mockAddress();

        String content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .body(addressDto)
                    .when()
                    .post()
                .then()
                    .statusCode(201)
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

    @Test
    @Order(3)
    public void testFindByID() throws JsonProcessingException {

        mockAddress();

        String content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParams("id", createdUserId)
                    .when()
                    .get("{id}")
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
        addressDto.setUserId(createdUserId);
        addressDto.setCity("São Paulo");
        addressDto.setState("São Paulo");
        addressDto.setNeighborhood("Vila Guilherme");
        addressDto.setPublicPlace("Rua Eugênio de Freitas");
        addressDto.setNumber("669");
        addressDto.setZipCode("02063-000");
    }

    @Test
    @Order(4)
    public void testFindAll() throws JsonProcessingException {
          String content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();

          WrapperAddressDto wrapperAddressDto = objectMapper.readValue(content, WrapperAddressDto.class);
          List<AddressDto> listAddressDto = wrapperAddressDto.getEmbedded().getAddressDtoList();

          //assertTrue(wrapperAddressDto.getEmbedded() == null);
          assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/address/v1/1\"}}"));
          assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/address/v1?page=0&limit=15&direction=asc\"}}"));

          AddressDto address = listAddressDto.get(0);
          assertEquals("920", address.getNumber());

    }

    @Test
    @Order(5)
    public void testDelete() throws JsonProcessingException {

        mockAddress();

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParams("id", createdUserId)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }

}
