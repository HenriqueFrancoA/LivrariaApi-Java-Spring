package br.com.henrique.JWT.services;

import br.com.henrique.JWT.enums.Status;
import br.com.henrique.JWT.exceptions.InvalidStatusException;
import br.com.henrique.JWT.exceptions.PaymentAlreadyExistsException;
import br.com.henrique.JWT.exceptions.StatusException;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.Payment;
import br.com.henrique.JWT.models.dto.PaymentDto;
import br.com.henrique.JWT.models.dto.PaymentOrderDto;
import br.com.henrique.JWT.models.dto.PaymentStatusDto;
import br.com.henrique.JWT.repositorys.OrderRepository;
import br.com.henrique.JWT.repositorys.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;


    public PaymentDto findById(Long id) {
        logger.info("Procurando Pagamento do ID: " + id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("ID do Pagamento não encontrado."));

        return DozerMapper.parseObject(payment, PaymentDto.class);
    }

    public PaymentDto save(PaymentOrderDto paymentOrderDto) {
        logger.info("Criando Pagamento.");
        Order order = orderRepository.findById(paymentOrderDto.getOrderId())
                .orElseThrow(() ->  new EntityNotFoundException("Pedido não encontrado."));

        Payment existPayment = paymentRepository.findByOrder(order);

        if(existPayment != null)
            throw new PaymentAlreadyExistsException("Pagamento já existente para o pedido do ID: " + order.getId());

        Payment payment = new Payment(
                null,
                order,
                paymentOrderDto.getPaymentMethod(),
                paymentOrderDto.getValue(),
                LocalDateTime.now(),
                Status.PENDING.getDescription()
        );

        return DozerMapper.parseObject(paymentRepository.save(payment), PaymentDto.class);
    }

    public void verifyStatus(String status){
        if(!status.equals(Status.PENDING.getDescription()) &&
                !status.equals(Status.APPROVED.getDescription()) &&
                !status.equals(Status.CANCELED.getDescription()) &&
                !status.equals(Status.REJECTED.getDescription()))
            throw new
                    InvalidStatusException("Insira um status correto, como: "
                    + Status.PENDING.getDescription() + ", "
                    + Status.APPROVED.getDescription() + ", "
                    + Status.CANCELED.getDescription() + " ou "
                    + Status.REJECTED.getDescription());
    }

    public PaymentDto updateStatus(Long id, PaymentStatusDto paymentStatusDto) {
        logger.info("Atualizando status do Pagamento, ID: " + id);

        verifyStatus(paymentStatusDto.getStatus());

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Pagamento não encontrado."));

        if(!payment.getStatus().equals(Status.PENDING.getDescription()))
            throw new StatusException("O pedido não pode ser atualizado, pois seu status se encontra: " + payment.getStatus());

        payment.setStatus(paymentStatusDto.getStatus());

        return DozerMapper.parseObject(paymentRepository.save(payment), PaymentDto.class);
    }
}
