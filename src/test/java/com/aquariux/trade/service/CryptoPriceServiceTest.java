package com.aquariux.trade.service;

import static com.aquariux.trade.constant.ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aquariux.trade.entity.CryptoPriceEntity;
import com.aquariux.trade.model.enums.ProvideSource;
import com.aquariux.trade.model.enums.TradingPair;
import com.aquariux.trade.model.response.BestAggregatedPriceResponseDTO;
import com.aquariux.trade.repository.CryptoPriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CryptoPriceServiceTest {

  @Mock
  private CryptoPriceRepository cryptoPriceRepository;

  @InjectMocks
  private CryptoPriceService cryptoPriceService;

  @Test
  void testGetLatestPrices_with_data_success() {
    CryptoPriceEntity ethPrice = new CryptoPriceEntity(UUID.randomUUID(), TradingPair.ETHUSDT,
        ProvideSource.AGGREGATED, new BigDecimal("2511.75"), new BigDecimal("2512.00"),
        LocalDateTime.now());
    CryptoPriceEntity btcPrice = new CryptoPriceEntity(UUID.randomUUID(), TradingPair.BTCUSDT,
        ProvideSource.AGGREGATED, new BigDecimal("60011.75"), new BigDecimal("60012.00"),
        LocalDateTime.now().minusSeconds(1));
    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.ETHUSDT)).thenReturn(
        List.of(ethPrice));
    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.BTCUSDT)).thenReturn(
        List.of(btcPrice));

    List<BestAggregatedPriceResponseDTO> prices = cryptoPriceService.getLatestBestAggregatedPrices();

    assertEquals(2, prices.size());
    assertEquals(TradingPair.ETHUSDT, prices.getFirst().getTradingPair());
    assertEquals(new BigDecimal("2511.75"), prices.get(0).getBidPrice());
    assertEquals(new BigDecimal("2512.00"), prices.get(0).getAskPrice());
    assertEquals(TradingPair.BTCUSDT, prices.get(1).getTradingPair());
    assertEquals(new BigDecimal("60011.75"), prices.get(1).getBidPrice());
    assertEquals(new BigDecimal("60012.00"), prices.get(1).getAskPrice());
    verify(cryptoPriceRepository, times(2)).findByProvideSourceAndTradingPair(
        eq(ProvideSource.AGGREGATED),
        any());
  }

  @Test
  void testGetLatestPrices_with_noData_success() {
    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.ETHUSDT)).thenReturn(
        Collections.emptyList());
    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.BTCUSDT)).thenReturn(
        Collections.emptyList());

    List<BestAggregatedPriceResponseDTO> prices = cryptoPriceService.getLatestBestAggregatedPrices();

    assertEquals(2, prices.size());
    assertEquals(TradingPair.ETHUSDT, prices.getFirst().getTradingPair());
    assertEquals(TRADE_FORMAT_AMOUNT_ZERO, prices.get(0).getBidPrice());
    assertEquals(TRADE_FORMAT_AMOUNT_ZERO, prices.get(0).getAskPrice());
    assertEquals(TradingPair.BTCUSDT, prices.get(1).getTradingPair());
    assertEquals(TRADE_FORMAT_AMOUNT_ZERO, prices.get(1).getBidPrice());
    assertEquals(TRADE_FORMAT_AMOUNT_ZERO, prices.get(1).getAskPrice());
  }
}
