package com.aquariux.trade.service;

import static com.aquariux.trade.constant.ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aquariux.trade.entity.CryptoPriceEntity;
import com.aquariux.trade.entity.TradeHistoryEntity;
import com.aquariux.trade.entity.UserWalletEntity;
import com.aquariux.trade.model.enums.Currency;
import com.aquariux.trade.model.enums.OrderType;
import com.aquariux.trade.model.enums.ProvideSource;
import com.aquariux.trade.model.enums.TradingPair;
import com.aquariux.trade.model.request.TradeCryptoOrderRequestDTO;
import com.aquariux.trade.model.response.TradeOrderResponseDTO;
import com.aquariux.trade.repository.CryptoPriceRepository;
import com.aquariux.trade.repository.TradeHistoryRepository;
import com.aquariux.trade.repository.UserWalletRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

  @Mock
  private CryptoPriceRepository cryptoPriceRepository;
  @Mock
  private UserWalletRepository userWalletRepository;
  @Mock
  private TradeHistoryRepository tradeHistoryRepository;

  @InjectMocks
  private TradeService tradeService;

  private static final String userId = "ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff";

  @Test
  void testExecuteTrade_buySuccess() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(TradingPair.ETHUSDT);
    request.setOrderType(OrderType.BUY);
    request.setQuantity(new BigDecimal("10.5"));

    CryptoPriceEntity price = new CryptoPriceEntity(UUID.randomUUID(), TradingPair.ETHUSDT,
        ProvideSource.AGGREGATED,
        new BigDecimal("2500.75"), new BigDecimal("2501.00"), LocalDateTime.now());
    UserWalletEntity usdtWallet = new UserWalletEntity(UUID.randomUUID(), userId, Currency.USDT,
        new BigDecimal("50000.0"));
    UserWalletEntity ethWallet = new UserWalletEntity(UUID.randomUUID(), userId, Currency.ETH,
        TRADE_FORMAT_AMOUNT_ZERO);

    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.ETHUSDT)).thenReturn(
        Collections.singletonList(price));
    when(userWalletRepository.findByUserIdAndCurrency(userId, Currency.USDT)).thenReturn(
        usdtWallet);
    when(userWalletRepository.findByUserIdAndCurrency(userId, Currency.ETH)).thenReturn(ethWallet);

    TradeOrderResponseDTO result = tradeService.executeTradeCryptoOrder(userId, request);

    assertEquals("Trade executed successfully", result.getMessage());
    assertEquals(0, new BigDecimal("23739.5").compareTo(usdtWallet.getBalance()));
    assertEquals(0, new BigDecimal("10.5").compareTo(ethWallet.getBalance()));
    verify(userWalletRepository, times(2)).findByUserIdAndCurrency(any(), any());
    verify(userWalletRepository, times(0)).save(any(UserWalletEntity.class));
    verify(tradeHistoryRepository).save(any(TradeHistoryEntity.class));
  }

  @Test
  void testExecuteTrade_with_insufficientBalance() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(TradingPair.ETHUSDT);
    request.setOrderType(OrderType.BUY);
    request.setQuantity(new BigDecimal("110.0"));

    CryptoPriceEntity price = new CryptoPriceEntity(UUID.randomUUID(), TradingPair.ETHUSDT,
        ProvideSource.AGGREGATED,
        new BigDecimal("2500.75"), new BigDecimal("2501.07"), LocalDateTime.now());
    UserWalletEntity usdtWallet = new UserWalletEntity(UUID.randomUUID(), userId, Currency.USDT,
        new BigDecimal("50000.0"));
    UserWalletEntity ethWallet = new UserWalletEntity(UUID.randomUUID(), userId, Currency.ETH,
        TRADE_FORMAT_AMOUNT_ZERO);

    when(cryptoPriceRepository.findByProvideSourceAndTradingPair(ProvideSource.AGGREGATED,
        TradingPair.ETHUSDT)).thenReturn(
        Collections.singletonList(price));
    when(userWalletRepository.findByUserIdAndCurrency(userId, Currency.USDT)).thenReturn(
        usdtWallet);
    when(userWalletRepository.findByUserIdAndCurrency(userId, Currency.ETH)).thenReturn(ethWallet);

    assertThrows(IllegalStateException.class,
        () -> tradeService.executeTradeCryptoOrder(userId, request));
    verify(userWalletRepository, never()).save(any());
    verify(tradeHistoryRepository, never()).save(any());
  }

  @Test
  void testExecuteTrade_with_invalidPair() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(null);
    request.setOrderType(OrderType.BUY);
    request.setQuantity(new BigDecimal("0.5"));

    assertThrows(RuntimeException.class,
        () -> tradeService.executeTradeCryptoOrder(userId, request));
  }

  @Test
  void testExecuteTrade_with_invalidOrderBy() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(TradingPair.ETHUSDT);
    request.setOrderType(null);
    request.setQuantity(new BigDecimal("0.5"));

    assertThrows(RuntimeException.class,
        () -> tradeService.executeTradeCryptoOrder(userId, request));
  }

  @Test
  void testExecuteTrade_with_invalidOrderByAndTradingPair() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(null);
    request.setOrderType(null);
    request.setQuantity(new BigDecimal("0.5"));

    assertThrows(RuntimeException.class,
        () -> tradeService.executeTradeCryptoOrder(userId, request));
  }

  @Test
  void testExecuteTrade_with_invalidAmount() {
    TradeCryptoOrderRequestDTO request = new TradeCryptoOrderRequestDTO();
    request.setTradingPair(TradingPair.ETHUSDT);
    request.setOrderType(OrderType.BUY);
    request.setQuantity(new BigDecimal("0.00"));

    assertThrows(RuntimeException.class,
        () -> tradeService.executeTradeCryptoOrder(userId, request));
  }
}
