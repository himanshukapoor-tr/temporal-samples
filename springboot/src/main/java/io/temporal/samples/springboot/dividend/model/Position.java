package io.temporal.samples.springboot.dividend.model;

public class Position {
  private final String securityAccountNo;
  private final int quantity;
  private final String isin;

  public Position(String securityAccountNo, int quantity, String isin) {
    this.securityAccountNo = securityAccountNo;
    this.quantity = quantity;
    this.isin = isin;
  }

  public String getSecurityAccountNo() {
    return securityAccountNo;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getIsin() {
    return isin;
  }
}
