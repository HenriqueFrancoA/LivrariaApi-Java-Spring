package br.com.henrique.JWT.integrationtests.controller;

import br.com.henrique.JWT.configs.TestConfigs;
import br.com.henrique.JWT.integrationtests.models.vo.TokenVO;
import br.com.henrique.JWT.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.henrique.JWT.models.Permission;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.vo.AccountCredentialsVO;
import br.com.henrique.JWT.repositorys.PermissionRepository;
import br.com.henrique.JWT.repositorys.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends AbstractIntegrationTest {

   private static TokenVO tokenVO;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Test
    @Order(0)
    public void setupTestUserAndPermissions() {
        Permission permission = new Permission();
        permission.setDescription("ROLE_USER");
        permissionRepository.save(permission);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("",
                8, 185000,
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
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

    }

    @Test
    @Order(1)
    public void testSignin() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("testuser", "123456");

        tokenVO = given()
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
                .as(TokenVO.class);

       assertNotNull(tokenVO.getAccessToken());
       assertNotNull(tokenVO.getRefreshToken());

    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO("testuser", "123456");

        TokenVO newTokenVO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                    .pathParams("username", tokenVO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                .   statusCode(200)
                .extract()
                    .body()
                        .as(TokenVO.class);

       assertNotNull(newTokenVO.getAccessToken());
       assertNotNull(newTokenVO.getRefreshToken());

    }

}
