package com.aquariux.trade.repository;

import com.aquariux.trade.entity.CryptoPriceEntity;
import com.aquariux.trade.model.enums.ProvideSource;
import com.aquariux.trade.model.enums.TradingPair;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoPriceRepository extends JpaRepository<CryptoPriceEntity, Long> {

  List<CryptoPriceEntity> findByProvideSourceAndTradingPair(ProvideSource provideSource, TradingPair tradingPair);

}
