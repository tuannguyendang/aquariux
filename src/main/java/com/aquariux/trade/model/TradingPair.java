package com.aquariux.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradingPair {
  ETHUSDT("ETH", "USDT"),
  BTCUSDT("BTC", "USDT");

  private final String baseCurrency;
  private final String quoteCurrency;

  public static TradingPair fromString(String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid trading pair: " + value);
    }
  }

  @Override
  public String toString() {
    return name();
  }
}
