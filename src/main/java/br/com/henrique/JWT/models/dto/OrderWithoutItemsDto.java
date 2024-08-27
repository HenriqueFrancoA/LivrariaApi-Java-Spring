package br.com.henrique.JWT.models.dto;

import br.com.henrique.JWT.models.User;

import java.time.LocalDateTime;

public class OrderWithoutItemsDto {

    private Long id;
    private UserDto user;
    private LocalDateTime orderDate;
    private String status;

    public OrderWithoutItemsDto() {
    }

    public OrderWithoutItemsDto(Long id, UserDto user, LocalDateTime orderDate, String status) {
        this.id = id;
        this.user = user;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
