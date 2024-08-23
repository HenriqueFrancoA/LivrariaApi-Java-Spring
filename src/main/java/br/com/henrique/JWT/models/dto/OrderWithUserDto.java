package br.com.henrique.JWT.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderWithUserDto {

    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private List<ItemOrderWithBookDto> items;

    public OrderWithUserDto() {
    }

    public OrderWithUserDto(Long userId, LocalDateTime orderDate, String status, List<ItemOrderWithBookDto> items) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public List<ItemOrderWithBookDto> getItems() {
        return items;
    }

    public void setItems(List<ItemOrderWithBookDto> items) {
        this.items = items;
    }
}
