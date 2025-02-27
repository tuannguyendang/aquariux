package com.aquariux.trade.model;

import com.aquariux.trade.model.enums.Currency;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {

  private Currency currency;
  private BigDecimal balance;

}
