package br.com.henrique.JWT.integrationtests.repositories;

import br.com.henrique.JWT.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderRepositoryTest extends AbstractIntegrationTest {


    @Autowired
    public OrderRepository orderRepository;

    private static Order order;

    @BeforeAll
    public static void setup(){
        order = new Order();
    }

    @Test
    @org.junit.jupiter.api.Order(0)
    public void testFindByUser() throws JsonProcessingException {
        //User user = new User();

        //order = orderRepository.findByUser(user).get(0);

        List<Order> orderList = orderRepository.findAllWithItems();

        assertTrue(orderList.isEmpty());
        //assertNotNull(order.getId());
        //assertNotNull(order.getUser());
        //assertNotNull(order.getOrderDate());
        //assertNotNull(order.getStatus());
        //assertNotNull(order.getItems());
        //assertNotNull(order.getAddress());

        //assertEquals("Brasil", createdAddress.getCountry());
        //assertEquals("São Paulo", createdAddress.getCity());
        //assertEquals("São Paulo", createdAddress.getState());
        //assertEquals("Vila Guilherme", createdAddress.getNeighborhood());
        //assertEquals("Rua Eugênio de Freitas", createdAddress.getPublicPlace());
        //assertEquals("669", createdAddress.getNumber());
        //assertEquals("02063-000", createdAddress.getZipCode());
    }

}
