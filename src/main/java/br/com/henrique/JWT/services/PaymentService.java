package br.com.henrique.JWT.services;

import br.com.henrique.JWT.enums.PaymentStatus;
import br.com.henrique.JWT.mapper.DozerMapper;
import br.com.henrique.JWT.models.Payment;
import br.com.henrique.JWT.models.dto.PaymentDto;
import br.com.henrique.JWT.models.dto.PaymentStatusDto;
import br.com.henrique.JWT.repositorys.OrderRepository;
import br.com.henrique.JWT.repositorys.PaymentRepository;
import br.com.henrique.JWT.utils.ValidationUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PaymentDto updateStatus(Long id, PaymentStatusDto paymentStatusDto) {
        logger.info("Atualizando status do Pagamento, ID: " + id);

        ValidationUtils.verifyPaymentStatus(paymentStatusDto.getStatus());

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Pagamento não encontrado."));

        ValidationUtils.verifyWaiting(payment.getStatus());

        payment.setStatus(paymentStatusDto.getStatus());

        return DozerMapper.parseObject(paymentRepository.save(payment), PaymentDto.class);
    }
}
