package com.aquariux.trade.model.response;

import com.aquariux.trade.model.BalanceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {

  private String userId;
  private List<BalanceDTO> balances;

}
