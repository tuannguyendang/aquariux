package com.aquariux.trade.model.request;

import com.aquariux.trade.model.enums.OrderType;
import com.aquariux.trade.model.enums.TradingPair;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeCryptoOrderRequestDTO {

  private TradingPair tradingPair;
  private OrderType orderType;
  private BigDecimal quantity;
}