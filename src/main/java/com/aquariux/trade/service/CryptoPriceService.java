package com.aquariux.trade.service;

import static com.aquariux.trade.constant.ApplicationConstant.ASK;
import static com.aquariux.trade.constant.ApplicationConstant.ASK_PRICE;
import static com.aquariux.trade.constant.ApplicationConstant.BID;
import static com.aquariux.trade.constant.ApplicationConstant.BID_PRICE;
import static com.aquariux.trade.constant.ApplicationConstant.DATA;
import static com.aquariux.trade.constant.ApplicationConstant.MAX_VALUE;
import static com.aquariux.trade.constant.ApplicationConstant.SYMBOL;
import static com.aquariux.trade.constant.ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO;

import com.aquariux.trade.config.AppConfig;
import com.aquariux.trade.entity.CryptoPriceEntity;
import com.aquariux.trade.model.enums.ProvideSource;
import com.aquariux.trade.model.enums.TradingPair;
import com.aquariux.trade.model.response.BestAggregatedPriceResponseDTO;
import com.aquariux.trade.repository.CryptoPriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class CryptoPriceService {

  private final CryptoPriceRepository cryptoPriceRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final AppConfig appConfig;
  private static final List<TradingPair> SUPPORTED_PAIRS = Arrays.asList(TradingPair.values());

  @Scheduled(fixedRate = 10000)
  public void fetchAndAggregatePrices() {
    Map<TradingPair, BigDecimal> bestBidPrices = new HashMap<>();
    Map<TradingPair, BigDecimal> bestAskPrices = new HashMap<>();
    SUPPORTED_PAIRS.forEach(pair -> {
      bestBidPrices.put(pair, TRADE_FORMAT_AMOUNT_ZERO);
      bestAskPrices.put(pair, MAX_VALUE);
    });

    fetchFromBinance(bestBidPrices, bestAskPrices);
    fetchFromHuobi(bestBidPrices, bestAskPrices);
    saveAggregatedPrices(bestBidPrices, bestAskPrices);
  }

  private void fetchFromBinance(Map<TradingPair, BigDecimal> bestBidPrices,
      Map<TradingPair, BigDecimal> bestAskPrices) {
    try {
      List<Map<String, String>> tickers = restTemplate.getForObject(appConfig.getBinanURL(),
          List.class);
      if (tickers != null) {
        for (Map<String, String> ticker : tickers) {
          String symbol = ticker.get(SYMBOL);
          try {
            TradingPair pair = TradingPair.fromString(symbol);
            BigDecimal bidPrice = new BigDecimal(ticker.get(BID_PRICE));
            BigDecimal askPrice = new BigDecimal(ticker.get(ASK_PRICE));
            bestBidPrices.put(pair, bestBidPrices.get(pair).max(bidPrice));
            bestAskPrices.put(pair, bestAskPrices.get(pair).min(askPrice));
            cryptoPriceRepository.save(
                CryptoPriceEntity.builder().tradingPair(pair).provideSource(ProvideSource.BINANCE)
                    .bidPrice(bidPrice).askPrice(askPrice).timestamp(LocalDateTime.now()).build());
          } catch (IllegalArgumentException ex) {
            log.error("Error process data from Binance {}, {}", ex.getMessage(), ex.getStackTrace());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error fetching data from Binance: {}, {}", e.getMessage(), e.getStackTrace());
    }
  }

  private void fetchFromHuobi(Map<TradingPair, BigDecimal> bestBidPrices,
      Map<TradingPair, BigDecimal> bestAskPrices) {
    try {
      Map<String, Object> response = restTemplate.getForObject(appConfig.getHoubiURL(), Map.class);
      if (response != null && response.containsKey(DATA)) {
        List<Map<String, Object>> tickers = (List<Map<String, Object>>) response.get("data");
        for (Map<String, Object> ticker : tickers) {
          String symbol = ((String) ticker.get("symbol")).toUpperCase();
          try {
            TradingPair pair = TradingPair.fromString(symbol);
            BigDecimal bidPrice = new BigDecimal(ticker.get(BID).toString());
            BigDecimal askPrice = new BigDecimal(ticker.get(ASK).toString());
            bestBidPrices.put(pair, bestBidPrices.get(pair).max(bidPrice));
            bestAskPrices.put(pair, bestAskPrices.get(pair).min(askPrice));
            cryptoPriceRepository.save(
                CryptoPriceEntity.builder().tradingPair(pair).provideSource(ProvideSource.HUOBI)
                    .bidPrice(bidPrice).askPrice(askPrice).timestamp(
                        LocalDateTime.now()).build());
          } catch (IllegalArgumentException ex) {
            log.error("Error process data from Huobi {}, {}",ex.getMessage(), ex.getStackTrace());
          }
        }
      }
    } catch (Exception e) {
      log.error("Error fetching data from Huobi: {}, {}",e.getMessage(), e.getStackTrace());
    }
  }

  private void saveAggregatedPrices(Map<TradingPair, BigDecimal> bestBidPrices,
      Map<TradingPair, BigDecimal> bestAskPrices) {
    SUPPORTED_PAIRS.forEach(pair ->
        cryptoPriceRepository.save(
            CryptoPriceEntity.builder()
                .tradingPair(pair).provideSource(ProvideSource.AGGREGATED)
                .bidPrice(bestBidPrices.get(pair)).askPrice(
                    bestAskPrices.get(pair)).timestamp(LocalDateTime.now()).build()));
  }

  /**
   * method get latest best aggregated prices
   *
   * @return List<BestAggregatedPriceResponseDTO>
   */
  public List<BestAggregatedPriceResponseDTO> getLatestBestAggregatedPrices() {
    return SUPPORTED_PAIRS.stream()
        .map(tradingPair -> cryptoPriceRepository.findByProvideSourceAndTradingPair(
                ProvideSource.AGGREGATED,
                tradingPair)
            .stream()
            .max(Comparator.comparing(CryptoPriceEntity::getTimestamp))
            .map(p -> new BestAggregatedPriceResponseDTO(p.getTradingPair().toString(),
                p.getBidPrice(),
                p.getAskPrice()))
            .orElse(
                new BestAggregatedPriceResponseDTO(tradingPair.toString(), TRADE_FORMAT_AMOUNT_ZERO,
                    TRADE_FORMAT_AMOUNT_ZERO)))
        .collect(Collectors.toList());
  }
}