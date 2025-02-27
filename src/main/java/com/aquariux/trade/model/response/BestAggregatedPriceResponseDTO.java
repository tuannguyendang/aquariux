package com.aquariux.trade.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BestAggregatedPriceResponseDTO {

  private String tradingPair;
  private BigDecimal bidPrice;
  private BigDecimal askPrice;

}