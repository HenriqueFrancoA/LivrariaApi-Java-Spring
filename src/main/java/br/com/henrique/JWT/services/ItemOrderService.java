package br.com.henrique.JWT.services;

import br.com.henrique.JWT.enums.OrderStatus;
import br.com.henrique.JWT.exceptions.ListEmptyException;
import br.com.henrique.JWT.exceptions.OrderStatusException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Book;
import br.com.henrique.JWT.models.ItemOrder;
import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.dto.ItemOrderAddDto;
import br.com.henrique.JWT.models.dto.ItemOrderDto;
import br.com.henrique.JWT.models.dto.ItemOrderListDto;
import br.com.henrique.JWT.repositorys.BookRepository;
import br.com.henrique.JWT.repositorys.ItemOrderRepository;
import br.com.henrique.JWT.repositorys.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ItemOrderDto> addItem(ItemOrderListDto itemOrderListDto) {
        logger.info("Adicionando item ao pedido de ID: " + itemOrderListDto.getOrderId());

        if(itemOrderListDto.getItems() == null
                || itemOrderListDto.getItems().isEmpty())
            throw new ListEmptyException("Lista de Itens está vazia.");


        Order order = orderRepository.findById(itemOrderListDto.getOrderId())
                .orElseThrow(() ->  new EntityNotFoundException("Pedido não encontrado."));

        verifyStatus(order.getStatus());

        Book book;
        ItemOrder itemOrder;
        List<ItemOrder> items = new ArrayList<>();

        for (ItemOrderAddDto item : itemOrderListDto.getItems()){

            book = bookRepository.findById(item.getBookId())
                .orElseThrow(() ->  new EntityNotFoundException("Livro não encontrado."));

            itemOrder = new ItemOrder(
                order,
                book,
                item.getQuantity(),
                item.getUnitPrice()
            );

            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());

            bookRepository.save(book);
            itemOrderRepository.save(itemOrder);
            items.add(itemOrder);
        }
        return DozerMapper.parseListObjects(items, ItemOrderDto.class);
    }

    public void verifyStatus(String status){
        if(!status.equals(OrderStatus.PENDING.getDescription()))
            throw new OrderStatusException("O pedido não pode ser atualizado, pois seu status se encontra: " + status);

    }

    public void delete(Long id) {
        logger.info("Deletando Item do ID: " + id);
        ItemOrder itemOrder = itemOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado."));

        verifyStatus(itemOrder.getOrder().getStatus());

        Book book = bookRepository.findById(itemOrder.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado."));

        book.setStockQuantity(book.getStockQuantity() + itemOrder.getQuantity());

        bookRepository.save(book);
        itemOrderRepository.deleteById(id);
    }
}
