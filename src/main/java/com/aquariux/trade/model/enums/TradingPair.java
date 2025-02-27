package com.aquariux.trade.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@AllArgsConstructor
@Log4j2
public enum TradingPair {
  ETHUSDT("ETH", "USDT"),
  BTCUSDT("BTC", "USDT");

  private final String baseCurrency;
  private final String quoteCurrency;

  public static TradingPair fromString(String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error("Invalid trading pair: {}", value);
      throw new IllegalArgumentException("Invalid trading pair: " + value);
    }
  }

  @Override
  public String toString() {
    return name();
  }
}
