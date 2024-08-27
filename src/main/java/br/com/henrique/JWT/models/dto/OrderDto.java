package br.com.henrique.JWT.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto  {

    private Long id;
    private UserDto user;
    private LocalDateTime orderDate;
    private String status;
    private List<ItemOrderWithoutOrderDto> items;

    public OrderDto() {
    }

    public OrderDto(UserDto user, LocalDateTime orderDate, String status, List<ItemOrderWithoutOrderDto> items) {
        this.user = user;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
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

    public List<ItemOrderWithoutOrderDto> getItems() {
        return items;
    }

    public void setItems(List<ItemOrderWithoutOrderDto> items) {
        this.items = items;
    }
}
