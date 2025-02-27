package com.aquariux.trade.repository;

import com.aquariux.trade.entity.TradeHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository extends JpaRepository<TradeHistoryEntity, Long> {

  List<TradeHistoryEntity> findByUserIdOrderByTimestampDesc(String userId);
}