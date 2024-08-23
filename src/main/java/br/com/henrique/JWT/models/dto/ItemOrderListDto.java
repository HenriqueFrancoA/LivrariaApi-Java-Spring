package br.com.henrique.JWT.models.dto;

import java.util.List;

public class ItemOrderListDto {

    private Long orderId;
    private List<ItemOrderAddDto> items;


    public ItemOrderListDto() {
    }

    public ItemOrderListDto(Long orderId, List<ItemOrderAddDto> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<ItemOrderAddDto> getItems() {
        return items;
    }

    public void setItems(List<ItemOrderAddDto> items) {
        this.items = items;
    }
}
