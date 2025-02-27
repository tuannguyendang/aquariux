package com.aquariux.trade.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProvideSource {
  BINANCE("Binance"),
  HUOBI("Huobi"),
  AGGREGATED("Aggregated");

  private final String displayName;

  @Override
  public String toString() {
    return displayName;
  }

}
