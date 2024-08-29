package br.com.henrique.JWT.services;

import br.com.henrique.JWT.exceptions.ListEmptyException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Book;
import br.com.henrique.JWT.models.ItemOrder;
import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.Payment;
import br.com.henrique.JWT.models.dto.*;
import br.com.henrique.JWT.repositorys.BookRepository;
import br.com.henrique.JWT.repositorys.ItemOrderRepository;
import br.com.henrique.JWT.repositorys.OrderRepository;
import br.com.henrique.JWT.repositorys.PaymentRepository;
import br.com.henrique.JWT.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ItemOrderService {

    private final Logger logger = Logger.getLogger(ItemOrderService.class.getName());

    @Autowired
    private ItemOrderRepository itemOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<ItemOrderDto> addItem(ItemOrderListDto itemOrderListDto) {
        logger.info("Adicionando item ao pedido de ID: " + itemOrderListDto.getOrderId());

        if(itemOrderListDto.getItems() == null
                || itemOrderListDto.getItems().isEmpty())
            throw new ListEmptyException("Lista de Itens está vazia.");

        Order order = orderRepository.findById(itemOrderListDto.getOrderId())
                .orElseThrow(() ->  new EntityNotFoundException("Pedido não encontrado."));

        Payment payment = paymentRepository.findByOrder(order);

        ValidationUtils.verifyPending(order.getStatus());
        ValidationUtils.verifyWaiting(payment.getStatus());

        Book book;
        ItemOrder itemOrder;
        List<ItemOrder> items = new ArrayList<>();
        BigDecimal totalValue = BigDecimal.valueOf(0);

        logger.info("Inserindo item no pedido: " + order.getId());

        for (ItemOrderAddDto item : itemOrderListDto.getItems()){

            ValidationUtils.verifyValue(item.getUnitPrice());

            ValidationUtils.verifyQuantity(item.getQuantity());

            book = bookRepository.findById(item.getBookId())
                .orElseThrow(() ->  new EntityNotFoundException("Livro não encontrado."));

            itemOrder = new ItemOrder(
                order,
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

        logger.info("Atualizando o pagamento do ID: " + payment.getId());

        payment.setValue(payment.getValue().add(totalValue));

        return DozerMapper.parseListObjects(items, ItemOrderDto.class);
    }

    public void delete(Long id) {
        logger.info("Deletando Item do ID: " + id);
        ItemOrder itemOrder = itemOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado."));

        Payment payment = paymentRepository.findByOrder(itemOrder.getOrder());

        ValidationUtils.verifyPending(itemOrder.getOrder().getStatus());
        ValidationUtils.verifyWaiting(payment.getStatus());

        Book book = bookRepository.findById(itemOrder.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado."));

        int bookQuantityUpdated = ValidationUtils.verifyBookQuantity(book.getStockQuantity(), -itemOrder.getQuantity());

        BigDecimal itemsValue = itemOrder.getUnitPrice().multiply(BigDecimal.valueOf(itemOrder.getQuantity()));

        payment.setValue(payment.getValue().subtract(itemsValue));

        book.setStockQuantity(bookQuantityUpdated);

        bookRepository.save(book);
        itemOrderRepository.deleteById(id);
        paymentRepository.save(payment);
    }

    public ItemOrderDto update(Long id, ItemOrderQuantityDto itemOrderQuantityDto) {
        logger.info("Atualizando Item, ID: " + id);

        ValidationUtils.verifyQuantity(itemOrderQuantityDto.getQuantity());

        ItemOrder item = itemOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado."));

        Payment payment = paymentRepository.findByOrder(item.getOrder());

        ValidationUtils.verifyPending(item.getOrder().getStatus());
        ValidationUtils.verifyWaiting(payment.getStatus());

        int differenceAmount =  itemOrderQuantityDto.getQuantity() - item.getQuantity();
        BigDecimal oldItemValue = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        item.setQuantity(itemOrderQuantityDto.getQuantity());

        Book book = bookRepository.findById(item.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado."));

        int bookQuantityUpdated = ValidationUtils.verifyBookQuantity(book.getStockQuantity(), differenceAmount);

        BigDecimal newItemValue = item.getUnitPrice().multiply(BigDecimal.valueOf(itemOrderQuantityDto.getQuantity()));

        BigDecimal differenceValue = newItemValue.subtract(oldItemValue);

        payment.setValue(payment.getValue().add(differenceValue));

        book.setStockQuantity(bookQuantityUpdated);

        bookRepository.save(book);
        itemOrderRepository.save(item);
        paymentRepository.save(payment);

        return DozerMapper.parseObject( itemOrderRepository.save(item), ItemOrderDto.class);
    }

    public ItemOrderDto findById(Long id) {
        logger.info("Procurando Item do ID: " + id);
        ItemOrder item = itemOrderRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Item não encontrado."));

        return DozerMapper.parseObject(item, ItemOrderDto.class);
    }
}
