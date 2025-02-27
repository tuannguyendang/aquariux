package com.aquariux.trade.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ApplicationConstant {

  public static final BigDecimal TRADE_FORMAT_AMOUNT_ZERO = BigDecimal.ZERO.setScale(2,
      RoundingMode.HALF_EVEN);
  public static final BigDecimal INITIAL_WALLET_BALANCE = new BigDecimal("50000.0").setScale(8,
      RoundingMode.HALF_EVEN);
  public static final BigDecimal MAX_VALUE = new BigDecimal("999999999999.99999999");
  public static final Double FEE_RATE = 0.000;
  public static final String SYMBOL = "symbol";
  public static final String BID_PRICE = "bidPrice";
  public static final String ASK_PRICE = "askPrice";
  public static final String DATA =  "data";
  public static final String  BID =  "bid";
  public static final String ASK =  "ask";
}
