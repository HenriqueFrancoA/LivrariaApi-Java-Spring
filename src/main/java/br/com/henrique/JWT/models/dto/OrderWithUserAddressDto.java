package br.com.henrique.JWT.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderWithUserAddressDto {

    private Long userId;
    private Long addressId;
    private List<ItemOrderWithBookDto> items;
    private String paymentMethod;

    public OrderWithUserAddressDto() {
    }

    public OrderWithUserAddressDto(Long userId, Long addressId, List<ItemOrderWithBookDto> items, String paymentMethod) {
        this.userId = userId;
        this.addressId = addressId;
        this.items = items;
        this.paymentMethod = paymentMethod;
    }

    public Long getAddressId() {return addressId;}

    public void setAddressId(Long addressId) {this.addressId = addressId;}

    public String getPaymentMethod() {return paymentMethod;}

    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {this.userId = userId;}

    public List<ItemOrderWithBookDto> getItems() {return items;}

    public void setItems(List<ItemOrderWithBookDto> items) {this.items = items;}
}
