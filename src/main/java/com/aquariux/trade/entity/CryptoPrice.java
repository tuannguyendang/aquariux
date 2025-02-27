package com.aquariux.trade.entity;

import com.aquariux.trade.model.ProvideSource;
import com.aquariux.trade.model.TradingPair;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CryptoPrice")
public class CryptoPrice {

  @Id
  @Column(name = "Id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "TradingPair")
  @Enumerated(EnumType.STRING)
  private TradingPair tradingPair;

  @Column(name = "ProvideSource")
  @Enumerated(EnumType.STRING)
  private ProvideSource provideSource;

  @Column(name = "BidPrice")
  private BigDecimal bidPrice;

  @Column(name = "AskPrice")
  private BigDecimal askPrice;

  @Column(name = "TimeStamp", updatable = false)
  private LocalDateTime timestamp;
}
