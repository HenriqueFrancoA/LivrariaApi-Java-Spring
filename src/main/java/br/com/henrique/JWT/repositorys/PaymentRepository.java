package br.com.henrique.JWT.repositorys;

import br.com.henrique.JWT.models.Order;
import br.com.henrique.JWT.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.order = :order")
    Payment findByOrder(@Param("order") Order order);

}
