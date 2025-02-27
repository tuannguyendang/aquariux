package com.aquariux.trade.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aquariux.trade.constant.ApplicationConstant;
import com.aquariux.trade.entity.TradeHistoryEntity;
import com.aquariux.trade.model.enums.OrderType;
import com.aquariux.trade.model.enums.TradingPair;
import com.aquariux.trade.model.response.TradeHistoryResponseDTO;
import com.aquariux.trade.repository.TradeHistoryRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TradeHistoryServiceTest {

  @Mock
  private TradeHistoryRepository tradeHistoryRepository;
  @InjectMocks
  private TradeHistoryService tradeHistoryService;
  private static final String userId = "ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff";

  @Test
  void testGetTradeHistory_with_data_success() {
    TradeHistoryEntity trade = new TradeHistoryEntity(UUID.randomUUID(), userId,
        TradingPair.ETHUSDT, OrderType.BUY, new BigDecimal("0.57"), new BigDecimal("2502.07"),
        new BigDecimal("1251.57"), 0.0,
        ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO, LocalDateTime.now());
    when(tradeHistoryRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(
        List.of(trade));

    List<TradeHistoryResponseDTO> history = tradeHistoryService.getTradeHistory(userId);

    assertEquals(1, history.size());
    assertEquals(TradingPair.ETHUSDT, history.getFirst().getTradingPair());
    assertEquals(OrderType.BUY, history.getFirst().getOrderType());
    assertEquals(new BigDecimal("0.57"), history.getFirst().getQuantity());
    assertEquals(new BigDecimal("2502.07"), history.getFirst().getPrice());
    assertEquals(new BigDecimal("1251.57"), history.getFirst().getTotalCost());
    verify(tradeHistoryRepository, times(1)).findByUserIdOrderByTimestampDesc(userId);
  }

  @Test
  void testGetTradeHistory_with_data_empty_success() {
    when(tradeHistoryRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(
        Collections.emptyList());

    List<TradeHistoryResponseDTO> history = tradeHistoryService.getTradeHistory(userId);

    Assertions.assertTrue(history.isEmpty());
    verify(tradeHistoryRepository, times(1)).findByUserIdOrderByTimestampDesc(userId);
  }
}