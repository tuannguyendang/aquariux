package com.aquariux.trade.controller;

import com.aquariux.trade.model.request.TradeCryptoOrderRequestDTO;
import com.aquariux.trade.model.response.BestAggregatedPriceResponseDTO;
import com.aquariux.trade.model.response.TradeHistoryResponseDTO;
import com.aquariux.trade.model.response.TradeOrderResponseDTO;
import com.aquariux.trade.model.response.WalletResponseDTO;
import com.aquariux.trade.service.CryptoPriceService;
import com.aquariux.trade.service.TradeHistoryService;
import com.aquariux.trade.service.TradeService;
import com.aquariux.trade.service.WalletService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/trade")
@RequiredArgsConstructor
public class CryptoTradingController {

  private final CryptoPriceService cryptoPriceService;
  private final TradeService tradeService;
  private final WalletService walletService;
  private final TradeHistoryService tradeHistoryService;

  /**
   * API get Best Latest Aggregated Prices
   *
   * @return List<BestAggregatedPriceResponseDTO>
   */
  @GetMapping("/aggregation-prices/latest")
  public ResponseEntity<List<BestAggregatedPriceResponseDTO>> getLatestBestAggregatedPrices() {
    return ResponseEntity.ok(cryptoPriceService.getLatestBestAggregatedPrices());
  }

  /**
   * API trade crypto
   *
   * @param userId current userId
   * @param tradeCryptoOrderRequestDTO data request
   * @return TradeOrderResponseDTO
   */
  @PostMapping("/{userId}")
  public ResponseEntity<TradeOrderResponseDTO> tradeCryptoOrder(@PathVariable String userId,
      @RequestBody TradeCryptoOrderRequestDTO tradeCryptoOrderRequestDTO) {
    try {
      TradeOrderResponseDTO result = tradeService.executeTradeCryptoOrder(userId,
          tradeCryptoOrderRequestDTO);
      return ResponseEntity.ok(result);
    } catch (IllegalArgumentException | IllegalStateException e) {
      return ResponseEntity.badRequest().body(new TradeOrderResponseDTO(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .body(new TradeOrderResponseDTO("Server error: " + e.getMessage()));
    }
  }

  /**
   * Retrieve Wallet Balance of current user
   *
   * @param userId current userId
   * @return WalletResponseDTO
   */
  @GetMapping("/wallet/{userId}")
  public ResponseEntity<WalletResponseDTO> getWalletBalance(@PathVariable String userId) {
    return ResponseEntity.ok(walletService.getWalletBalance(userId));
  }

  /**
   * method retrieve trade history
   *
   * @param userId current userId
   * @return List<TradeHistoryResponseDTO>
   */
  @GetMapping("/history/{userId}")
  public ResponseEntity<List<TradeHistoryResponseDTO>> getTradeHistory(
      @PathVariable String userId) {
    return ResponseEntity.ok(tradeHistoryService.getTradeHistory(userId));
  }
}
