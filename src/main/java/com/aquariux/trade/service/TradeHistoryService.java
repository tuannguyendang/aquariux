package com.aquariux.trade.service;

import com.aquariux.trade.model.response.TradeHistoryResponseDTO;
import com.aquariux.trade.repository.TradeHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeHistoryService {

  private final TradeHistoryRepository tradeHistoryRepository;

  /**
   * Method read trade history by userId
   *
   * @param userId current userId
   * @return TradeHistoryResponseDTO
   */
  @Transactional(readOnly = true)
  public List<TradeHistoryResponseDTO> getTradeHistory(String userId) {
    return tradeHistoryRepository.findByUserIdOrderByTimestampDesc(userId)
        .stream()
        .map(history -> {
          TradeHistoryResponseDTO responseDTO = new TradeHistoryResponseDTO();
          BeanUtils.copyProperties(history, responseDTO);
          return responseDTO;
        })
        .collect(Collectors.toList());
  }
}
