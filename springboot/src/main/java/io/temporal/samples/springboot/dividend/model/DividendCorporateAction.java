package io.temporal.samples.springboot.dividend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DividendCorporateAction {
  private BigDecimal dividend;
  private LocalDateTime exDate;
  private UUID corporateActionId;

  public DividendCorporateAction() {}

  public DividendCorporateAction(
      BigDecimal dividend, LocalDateTime exDate, UUID corporateActionId) {
    this.dividend = dividend;
    this.exDate = exDate;
    this.corporateActionId = corporateActionId;
  }

  public BigDecimal getDividend() {
    return dividend;
  }

  public LocalDateTime getExDate() {
    return exDate;
  }

  public UUID getCorporateActionId() {
    return corporateActionId;
  }

  public void setDividend(BigDecimal dividend) {
    this.dividend = dividend;
  }

  public void setExDate(LocalDateTime exDate) {
    this.exDate = exDate;
  }

  public void setCorporateActionId(UUID corporateActionId) {
    this.corporateActionId = corporateActionId;
  }
}
