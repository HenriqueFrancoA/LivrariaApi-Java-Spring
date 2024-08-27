package br.com.henrique.JWT.services;

import br.com.henrique.JWT.enums.Status;
import br.com.henrique.JWT.exceptions.StatusException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Book;
import br.com.henrique.JWT.models.ItemOrder;
import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.User;
import br.com.henrique.JWT.models.dto.ItemOrderWithBookDto;
import br.com.henrique.JWT.models.dto.OrderDto;
import br.com.henrique.JWT.models.dto.OrderStatusDto;
import br.com.henrique.JWT.models.dto.OrderWithUserDto;
import br.com.henrique.JWT.repositorys.BookRepository;
import br.com.henrique.JWT.repositorys.ItemOrderRepository;
import br.com.henrique.JWT.repositorys.OrderRepository;
import br.com.henrique.JWT.repositorys.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class OrderService {

    private final Logger logger = Logger.getLogger(OrderService.class.getName());

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ItemOrderRepository itemOrderRepository;

    public OrderDto updateStatus(Long id, OrderStatusDto orderStatus) {
        logger.info("Atualizando status do Pedido, ID: " + id);
        Order ord = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("ID não encontrado."));

        if(ord.getStatus().equals(Status.REJECTED.getDescription()))
            throw new StatusException("O pedido já foi negado e não pode ser atualizado.");

        if(!ord.getStatus().equals(Status.REJECTED.getDescription()) && orderStatus.getStatus().equals(Status.REJECTED.getDescription())){
            Book book;
            for(ItemOrder item : ord.getItems()){
                book = bookRepository.findById(item.getBook().getId()).orElseThrow(() -> new EntityNotFoundException("ID do Livro não encontrado."));

                book.setStockQuantity(book.getStockQuantity() + item.getQuantity());

                bookRepository.save(book);
            }
        }

        ord.setStatus(orderStatus.getStatus());

        orderRepository.save(ord);

        return DozerMapper.parseObject(ord, OrderDto.class);
    }

    public OrderDto save(OrderWithUserDto orderWithUserDto) {
        logger.info("Criando pedido.");

        User user = userRepository.findById(orderWithUserDto.getUserId())
                .orElseThrow(() ->  new EntityNotFoundException("Usuário não encontrado."));

        Order order = new Order(
                orderWithUserDto,
                user
        );

       Order orderCreated = orderRepository.save(order);

       Book book;
       ItemOrder itemOrder;
       List<ItemOrder> items = new ArrayList<>();

        logger.info("Inserindo item no pedido: " + orderCreated.getId());

        for (ItemOrderWithBookDto item : orderWithUserDto.getItems()){

           book = bookRepository.findById(item.getBookId())
                   .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado."));

            itemOrder = new ItemOrder(
                    orderCreated,
                    book,
                    item.getQuantity(),
                    item.getUnitPrice()
            );

            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
            bookRepository.save(book);
            itemOrderRepository.save(itemOrder);
            items.add(itemOrder);
       }
        orderCreated.setItems(items);

        return DozerMapper.parseObject(orderCreated, OrderDto.class);
    }

    public OrderDto findById(Long id) {
        logger.info("Procurando pedido do ID: " + id);
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Pedido não encontrado."));

        return DozerMapper.parseObject(order, OrderDto.class);
    }

    public List<OrderDto> findAll() {
        logger.info("Retornando todos pedidos...");
        List<Order> listOrders = orderRepository.findAllWithItems();
        return DozerMapper.parseListObjects(listOrders, OrderDto.class);
    }
}
