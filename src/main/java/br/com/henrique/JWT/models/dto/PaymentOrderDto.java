package br.com.henrique.JWT.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class PaymentOrderDto {

    private Long orderId;
    private String paymentMethod;
    private BigDecimal value;

    public PaymentOrderDto() {
    }

    public PaymentOrderDto(Long orderId, String paymentMethod, BigDecimal value, LocalDateTime paymentDate, String status) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.value = value;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
