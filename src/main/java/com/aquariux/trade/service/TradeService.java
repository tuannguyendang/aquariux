package com.aquariux.trade.service;

import static com.aquariux.trade.constant.ApplicationConstant.FEE_RATE;
import static com.aquariux.trade.constant.ApplicationConstant.INITIAL_WALLET_BALANCE;
import static com.aquariux.trade.constant.ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO;

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
import java.util.Arrays;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TradeService {

  private final CryptoPriceRepository cryptoPriceRepository;
  private final UserWalletRepository userWalletRepository;
  private final TradeHistoryRepository tradeHistoryRepository;

  /**
   * Method execute trade order
   *
   * @param userId                     current userId
   * @param tradeCryptoOrderRequestDTO request body
   * @return result of order
   */
  @Transactional
  public TradeOrderResponseDTO executeTradeCryptoOrder(String userId,
      TradeCryptoOrderRequestDTO tradeCryptoOrderRequestDTO) {
    TradingPair tradingPair = tradeCryptoOrderRequestDTO.getTradingPair();
    OrderType orderType = tradeCryptoOrderRequestDTO.getOrderType();
    BigDecimal quantity = tradeCryptoOrderRequestDTO.getQuantity();

    if (quantity.compareTo(TRADE_FORMAT_AMOUNT_ZERO) <= 0) {
      throw new IllegalArgumentException("The quantity must be positive number.");
    }

    CryptoPriceEntity latestPrice = this.getCryptoPriceEntity(tradingPair);

    BigDecimal price =
        orderType == OrderType.BUY ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
    BigDecimal totalCost = price.multiply(quantity);
    Currency cryptoCurrency = Currency.fromString(tradingPair.getBaseCurrency());

    UserWalletEntity usdtWallet = this.getOrInitialUserWalletEntity(userId);
    UserWalletEntity cryptoWallet = this.getOrInitialUserWalletEntity(userId,
        cryptoCurrency);

    this.executeTrade(orderType, usdtWallet, totalCost, cryptoWallet, quantity, cryptoCurrency);

    userWalletRepository.saveAll(Arrays.asList(usdtWallet, cryptoWallet));
    tradeHistoryRepository.save(
        TradeHistoryEntity.builder().userId(userId).tradingPair(tradingPair).orderType(orderType)
            .quantity(quantity).price(price).totalCost(totalCost).timestamp(
                LocalDateTime.now()).feeAmount(TRADE_FORMAT_AMOUNT_ZERO)
            .feeRate(FEE_RATE).build());
    return new TradeOrderResponseDTO("Trade executed successfully");
  }

  private void executeTrade(OrderType orderType, UserWalletEntity usdtWallet,
      BigDecimal totalCost, UserWalletEntity cryptoWallet, BigDecimal quantity,
      Currency cryptoCurrency) {
    switch (orderType) {
      case BUY -> {
        if (usdtWallet.getBalance().compareTo(totalCost) < 0) {
          log.error("Insufficient USDT balance");
          throw new IllegalStateException("Insufficient USDT balance");
        }
        usdtWallet.setBalance(usdtWallet.getBalance().subtract(totalCost));
        cryptoWallet.setBalance(cryptoWallet.getBalance().add(quantity));
      }
      case SELL -> {
        if (cryptoWallet.getBalance().compareTo(quantity) < 0) {
          log.error("Insufficient {} balance", cryptoCurrency);
          throw new IllegalStateException("Insufficient " + cryptoCurrency + " balance");
        }
        cryptoWallet.setBalance(cryptoWallet.getBalance().subtract(quantity));
        usdtWallet.setBalance(usdtWallet.getBalance().add(totalCost));
      }
      default -> {
        log.error("Order Type  {} doesn't support yet.", orderType);
        throw new IllegalStateException("Order Type " + orderType + " doesn't support yet.");
      }
    }
  }

  private UserWalletEntity getOrInitialUserWalletEntity(String userId, Currency cryptoCurrency) {
    UserWalletEntity cryptoWallet = userWalletRepository.findByUserIdAndCurrency(userId,
        cryptoCurrency);
    if (cryptoWallet == null) {
      cryptoWallet = UserWalletEntity.builder().userId(userId).currency(cryptoCurrency)
          .balance(TRADE_FORMAT_AMOUNT_ZERO).build();
      userWalletRepository.save(cryptoWallet);
    }
    return cryptoWallet;
  }

  private UserWalletEntity getOrInitialUserWalletEntity(String userId) {
    UserWalletEntity usdtWallet = userWalletRepository.findByUserIdAndCurrency(userId,
        Currency.USDT);
    if (usdtWallet == null) {
      usdtWallet = UserWalletEntity.builder().userId(userId).currency(Currency.USDT)
          .balance(INITIAL_WALLET_BALANCE).build();
      userWalletRepository.save(usdtWallet);
    }
    return usdtWallet;
  }

  private CryptoPriceEntity getCryptoPriceEntity(TradingPair tradingPair) {
    return cryptoPriceRepository.findByProvideSourceAndTradingPair(
            ProvideSource.AGGREGATED, tradingPair)
        .stream()
        .max(Comparator.comparing(CryptoPriceEntity::getTimestamp))
        .orElseThrow(() -> {
          log.error("No aggregated price data available.");
          return new RuntimeException("No aggregated price data available.");
        });
  }
}