package br.com.henrique.JWT.services;

import br.com.henrique.JWT.enums.OrderStatus;
import br.com.henrique.JWT.enums.PaymentStatus;
import br.com.henrique.JWT.exceptions.StatusException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.*;
import br.com.henrique.JWT.models.dto.*;
import br.com.henrique.JWT.repositories.*;
import br.com.henrique.JWT.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AddressRepository addressRepository;

    public OrderDto updateStatus(Long id, OrderStatusDto orderStatus) {
        logger.info("Atualizando status do Pedido, ID: " + id);

        ValidationUtils.verifyOrderStatus(orderStatus.getStatus());

        Order ord = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("ID não encontrado."));

        if(verifyCanceledOrRejected(ord.getStatus()))
            throw new StatusException("O pedido já foi " + ord.getStatus() + " e não pode ser atualizado.");

        if(verifyCanceledOrRejected(ord.getStatus()) &&
                verifyCanceledOrRejected(orderStatus.getStatus())){
            Book book;
            for(ItemOrder item : ord.getItems()){
                book = bookRepository.findById(item.getBook().getId())
                        .orElseThrow(() -> new EntityNotFoundException("ID do Livro não encontrado."));

                book.setStockQuantity(book.getStockQuantity() + item.getQuantity());

                bookRepository.save(book);
            }
        }

        ord.setStatus(orderStatus.getStatus());

        orderRepository.save(ord);

        return DozerMapper.parseObject(ord, OrderDto.class);
    }

    public OrderDto updateAddress(Long id, OrderAddressDto orderAddressDto) {
        logger.info("Atualizando status do Pedido, ID: " + id);

        Order ord = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new EntityNotFoundException("ID do Pedido não encontrado."));

        ValidationUtils.verifyPending(ord.getStatus());

        Address address = addressRepository.findById(orderAddressDto.getAddress())
                .orElseThrow(() -> new EntityNotFoundException("ID do Endereço não encontrado."));

        if(!address.getUserId().getId().equals(ord.getUser().getId()))
            throw new IllegalArgumentException("O Endereço não pertence ao Usuário informado.");

        ord.setAddress(address);

        orderRepository.save(ord);

        return DozerMapper.parseObject(ord, OrderDto.class);
    }

    public boolean verifyCanceledOrRejected(String status){
        return status.equals(OrderStatus.REJECTED.getDescription()) ||
                status.equals(OrderStatus.CANCELED.getDescription());
    }

    public PaymentDto save(OrderWithUserAddressDto orderWithUserAddressDto) {
        logger.info("Criando pedido...");

        ValidationUtils.verifyPaymentMethod(orderWithUserAddressDto.getPaymentMethod());

        User user = userRepository.findById(orderWithUserAddressDto.getUserId())
                .orElseThrow(() ->  new EntityNotFoundException("Usuário não encontrado."));

        Address address = addressRepository.findById(orderWithUserAddressDto.getAddressId())
                .orElseThrow(() ->  new EntityNotFoundException("Endereço não encontrado."));

        if(!address.getUserId().getId().equals(user.getId()))
            throw new IllegalArgumentException("O Endereço não pertence ao Usuário informado.");

        Order order = new Order(
                orderWithUserAddressDto,
                user,
                address,
                LocalDateTime.now(),
                OrderStatus.PENDING.getDescription()
        );

       Order orderCreated = orderRepository.save(order);

       Book book;
       ItemOrder itemOrder;
       List<ItemOrder> items = new ArrayList<>();
       BigDecimal totalValue = BigDecimal.valueOf(0);

        logger.info("Inserindo item no pedido: " + orderCreated.getId());

        for (ItemOrderWithBookDto item : orderWithUserAddressDto.getItems()){

            ValidationUtils.verifyValue(item.getUnitPrice());

            ValidationUtils.verifyQuantity(item.getQuantity());

            book = bookRepository.findById(item.getBookId())
                   .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado."));

            itemOrder = new ItemOrder(
                    orderCreated,
                    book,
                    item.getQuantity(),
                    item.getUnitPrice()
            );

            int bookQuantityUpdated = ValidationUtils.verifyBookQuantity(book.getStockQuantity(), item.getQuantity());

            book.setStockQuantity(bookQuantityUpdated);

            bookRepository.save(book);
            itemOrderRepository.save(itemOrder);
            items.add(itemOrder);

            totalValue = totalValue.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
       }

        logger.info("Criando pagamento para o pedido: " + orderCreated.getId());

        orderCreated.setItems(items);

        Payment payment = new Payment(
                null,
                orderCreated,
                orderWithUserAddressDto.getPaymentMethod(),
                totalValue,
                LocalDateTime.now(),
                PaymentStatus.WAITING.getDescription()
        );

        paymentRepository.save(payment);

        return DozerMapper.parseObject(payment, PaymentDto.class);
    }

    public OrderDto findById(Long id) {
        logger.info("Procurando pedido do ID: " + id);
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Pedido não encontrado."));

        return DozerMapper.parseObject(order, OrderDto.class);
    }

    public List<OrderDto> findByUser(Long id) {
        logger.info("Procurando pedidos do User: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Usuário não encontrado."));

        return  DozerMapper.parseListObjects(orderRepository.findByUser(user), OrderDto.class);
    }


}
