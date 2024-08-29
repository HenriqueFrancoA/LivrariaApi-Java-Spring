package br.com.henrique.JWT.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import br.com.henrique.JWT.models.dto.OrderWithUserAddressDto;
import jakarta.persistence.*;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private String status;

    @OneToMany(mappedBy = "order")
    private List<ItemOrder> items;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Order() {
    }

    public Order(OrderWithUserAddressDto orderWithUserDto, User user, Address address, LocalDateTime localDateTime, String status) {
        this.user = user;
        this.orderDate = localDateTime;
        this.status = status;
        this.address = address;
    }

    public Order(Long id, User user, LocalDateTime orderDate, String status, List<ItemOrder> items, Address address) {
        this.id = id;
        this.user = user;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemOrder> getItems() {
        return items;
    }

    public void setItems(List<ItemOrder> items) {
        this.items = items;
    }

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

}
