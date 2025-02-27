package com.aquariux.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
  BUY("BUY"),
  SELL("SELL");

  private final String type;

  public static OrderType fromString(String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid order type: " + value);
    }
  }

  @Override
  public String toString() {
    return type;
  }

}
