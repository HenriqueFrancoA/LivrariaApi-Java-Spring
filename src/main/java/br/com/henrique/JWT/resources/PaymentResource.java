package br.com.henrique.JWT.resources;

import br.com.henrique.JWT.models.Payment;
import br.com.henrique.JWT.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pagamentos")
@RestController
@RequestMapping("/api/payments/v1")
public class PaymentResource extends GenericResource<Payment, Long>  {

    public PaymentResource(PaymentService paymentService) {
        super(paymentService);
    }

    @Override
    @Operation(summary = "Busca um pagamento pelo ID e caso exista atualiza o mesmo")
    public ResponseEntity<Payment> update(Long aLong, Payment entity) {
        return super.update(aLong, entity);
    }

}
