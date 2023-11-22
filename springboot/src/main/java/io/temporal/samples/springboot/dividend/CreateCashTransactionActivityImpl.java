package io.temporal.samples.springboot.dividend;

import io.temporal.samples.springboot.dividend.model.CashTransaction;
import io.temporal.samples.springboot.dividend.model.DividendCorporateAction;
import io.temporal.samples.springboot.dividend.model.Position;
import io.temporal.spring.boot.ActivityImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@ActivityImpl
public class CreateCashTransactionActivityImpl implements CreateCashTransactionActivity {

  @Autowired private CashTransactionRepository cashTransactionRepository;

  @Override
  public boolean saveCashTransactions(
      List<Position> positions, DividendCorporateAction dividendCorporateAction) {
    List<CashTransaction> cashTransactions =
        positions.stream()
            .map(
                position ->
                    new CashTransaction(
                        position.getSecurityAccountNo(),
                        dividendCorporateAction
                            .getDividend()
                            .multiply(BigDecimal.valueOf(position.getQuantity())),
                        dividendCorporateAction.getCorporateActionId()))
            .collect(Collectors.toList());
    cashTransactionRepository.saveAll(cashTransactions);
    return true;
  }
}
