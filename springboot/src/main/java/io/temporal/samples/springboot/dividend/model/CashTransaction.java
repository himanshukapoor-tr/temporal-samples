package io.temporal.samples.springboot.dividend.model;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class CashTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column private String accountNumber;

  @Column private BigDecimal amount;

  @Column private UUID corporateActionId;

  public CashTransaction() {}

  public CashTransaction(String accountNumber, BigDecimal amount, UUID corporateActionId) {
    this.accountNumber = accountNumber;
    this.amount = amount;
    this.corporateActionId = corporateActionId;
  }

  public Integer getId() {
    return id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public UUID getCorporateActionId() {
    return corporateActionId;
  }

  public void setCorporateActionId(UUID corporateActionId) {
    this.corporateActionId = corporateActionId;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
