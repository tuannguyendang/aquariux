package com.aquariux.trade.entity;

import com.aquariux.trade.model.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "UserWallet")
public class UserWalletEntity implements Serializable {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "Id", nullable = false)
  private UUID id;

  @Column(name = "UserId")
  private String userId;

  @Column(name = "Currency")
  @Enumerated(EnumType.STRING)
  private Currency currency;

  @Column(name = "Balance")
  private BigDecimal balance;
}
