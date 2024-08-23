package br.com.henrique.JWT.repositorys;

import br.com.henrique.JWT.models.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {



}
