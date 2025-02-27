package com.aquariux.trade.entity;

import com.aquariux.trade.model.enums.OrderType;
import com.aquariux.trade.model.enums.TradingPair;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

@Data
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TradeHistory")
public class TradeHistoryEntity implements Serializable {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "Id", nullable = false)
  private UUID id;

  @Column(name = "UserId")
  private String userId;

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
  private Double feeRate;

  @Column(name = "FeeAmount")
  private BigDecimal feeAmount;

  @Column(name = "TimeStamp", updatable = false)
  private LocalDateTime timestamp;

}
