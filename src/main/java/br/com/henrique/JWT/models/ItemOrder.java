package br.com.henrique.JWT.models;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Itemsorder")
public class ItemOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    public ItemOrder() {
    }

    public ItemOrder(Order order, Book book, Integer quantity, BigDecimal unitPrice) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public ItemOrder(Long id, Order order, Book book, Integer quantity, BigDecimal unitPrice) {
        this.id = id;
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public ItemOrder(ItemOrder itemOrder) {
        this.id = itemOrder.getId();
        this.order = itemOrder.getOrder();
        this.book = itemOrder.getBook();
        this.quantity = itemOrder.getQuantity();
        this.unitPrice = itemOrder.getUnitPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

}
