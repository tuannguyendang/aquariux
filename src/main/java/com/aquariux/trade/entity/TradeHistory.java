package com.aquariux.trade.entity;

import com.aquariux.trade.model.OrderType;
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
@Table(name = "TradeHistory")
public class TradeHistory {

  @Id
  @Column(name = "Id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "UserId")
  private Long userId;

  @Column(name = "TradingPair")
  @Enumerated(EnumType.STRING)
  private TradingPair tradingPair;

  @Column(name = "OrderType")
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column(name = "Quantity")
  private BigDecimal quantity;

  @Column(name = "Price")
  private BigDecimal price;

  @Column(name = "TotalCost")
  private BigDecimal totalCost;

  @Column(name = "FeeRate")
  private Double FeeRate;

  @Column(name = "FeeAmount")
  private BigDecimal FeeAmount;

  @Column(name = "TimeStamp", updatable = false)
  private LocalDateTime timestamp;

}
