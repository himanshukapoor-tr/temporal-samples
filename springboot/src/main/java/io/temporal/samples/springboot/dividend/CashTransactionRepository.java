package io.temporal.samples.springboot.dividend;

import io.temporal.samples.springboot.dividend.model.CashTransaction;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Integer> {
  List<CashTransaction> findByCorporateActionId(UUID corporateActionId);
}
