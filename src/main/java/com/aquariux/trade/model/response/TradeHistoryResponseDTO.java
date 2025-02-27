package com.aquariux.trade.model.response;

import com.aquariux.trade.model.enums.OrderType;
import com.aquariux.trade.model.enums.TradingPair;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeHistoryResponseDTO {

  private UUID id;
  private TradingPair tradingPair;
  private OrderType orderType;
  private BigDecimal quantity;
  private BigDecimal price;
  private BigDecimal totalCost;
  private LocalDateTime timestamp;

}
