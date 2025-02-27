package com.aquariux.trade.repository;

import com.aquariux.trade.entity.UserWalletEntity;
import com.aquariux.trade.model.enums.Currency;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

  UserWalletEntity findByUserIdAndCurrency(String userId, Currency currency);

  List<UserWalletEntity> findByUserId(String userId);
}