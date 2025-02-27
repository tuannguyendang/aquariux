package com.aquariux.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProvideSource {
  BINANCE("Binance"),
  HUOBI("Huobi"),
  AGGREGATED("Aggregated");

  private final String displayName;

  public static ProvideSource fromString(String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid source: " + value);
    }
  }

  @Override
  public String toString() {
    return displayName;
  }

}
