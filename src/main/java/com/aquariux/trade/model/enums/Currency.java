package com.aquariux.trade.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
  USDT("USDT"),
  ETH("ETH"),
  BTC("BTC");

  private final String symbol;

  public static Currency fromString(String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid currency: " + value);
    }
  }

  @Override
  public String toString() {
    return symbol;
  }

}
