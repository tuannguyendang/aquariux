package com.aquariux.trade.service;

import static com.aquariux.trade.constant.ApplicationConstant.INITIAL_WALLET_BALANCE;
import static com.aquariux.trade.constant.ApplicationConstant.TRADE_FORMAT_AMOUNT_ZERO;

import com.aquariux.trade.entity.UserWalletEntity;
import com.aquariux.trade.model.BalanceDTO;
import com.aquariux.trade.model.enums.Currency;
import com.aquariux.trade.model.response.WalletResponseDTO;
import com.aquariux.trade.repository.UserWalletRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

  private final UserWalletRepository userWalletRepository;
  private static final List<Currency> SUPPORTED_CURRENCIES = Arrays.asList(Currency.values());

  /**
   * Method get wallet balance by userId
   * @param userId
   * @return WalletResponseDTO
   */
  @Transactional
  public WalletResponseDTO getWalletBalance(String userId) {
    var result = new Object() {
      List<UserWalletEntity> wallets = userWalletRepository.findByUserId(userId);
    };
    if (result.wallets.isEmpty()) {
      this.initializeWallet(userId);
      result.wallets = userWalletRepository.findByUserId(userId);
    }

    List<BalanceDTO> balances = SUPPORTED_CURRENCIES.stream()
        .map(currency -> {
          UserWalletEntity wallet = result.wallets.stream()
              .filter(w -> w.getCurrency() == currency)
              .findFirst()
              .orElse(UserWalletEntity.builder().userId(userId).balance(TRADE_FORMAT_AMOUNT_ZERO).build());
          return new BalanceDTO(currency.toString(), wallet.getBalance());
        })
        .collect(Collectors.toList());

    return new WalletResponseDTO(userId, balances);
  }

  private void initializeWallet(String userId) {
    userWalletRepository.saveAll(Arrays.asList(
        UserWalletEntity.builder().userId(userId).currency(Currency.USDT)
            .balance(INITIAL_WALLET_BALANCE).build(),
        UserWalletEntity.builder().userId(userId).currency(Currency.ETH)
            .balance(TRADE_FORMAT_AMOUNT_ZERO)
            .build(),
        UserWalletEntity.builder().userId(userId).currency(Currency.BTC)
            .balance(TRADE_FORMAT_AMOUNT_ZERO)
            .build()
    ));
  }
}