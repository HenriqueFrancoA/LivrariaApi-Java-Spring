package br.com.henrique.JWT.services;

import br.com.henrique.JWT.models.Author;
import br.com.henrique.JWT.models.Payment;
import br.com.henrique.JWT.repositorys.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PaymentService extends GenericService<Payment, Long> {

    public PaymentService(PaymentRepository paymentRepository) {
        super(paymentRepository);
    }

    private final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment update(Long id, Payment payment, String simpleName) {
        logger.info("Atualizando Pagamento, ID: " + id);
        Payment pay = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID não encontrado."));

        pay = new Payment(payment);

        return paymentRepository.save(pay);
    }
}
