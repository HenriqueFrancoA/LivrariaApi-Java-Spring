package br.com.henrique.JWT.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.henrique.JWT.models.dto.PaymentOrderDto;
import jakarta.persistence.*;

@Entity
@Table(name = "Payments")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_method")
    private String paymentMethod;

    private BigDecimal value;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    private String status;

    public Payment() {
    }

    public Payment(Payment payment) {
        this.id = payment.getId();
        this.order = payment.getOrder();
        this.paymentMethod = payment.getPaymentMethod();
        this.value = payment.getValue();
        this.paymentDate = payment.getPaymentDate();
        this.status = payment.getStatus();
    }

    public Payment(Long id, Order order, String paymentMethod, BigDecimal value, LocalDateTime paymentDate,
                   String status) {
        this.id = id;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.value = value;
        this.paymentDate = paymentDate;
        this.status = status;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
