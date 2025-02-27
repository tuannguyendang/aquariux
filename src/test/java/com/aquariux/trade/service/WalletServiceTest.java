package com.aquariux.trade.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aquariux.trade.entity.UserWalletEntity;
import com.aquariux.trade.model.enums.Currency;
import com.aquariux.trade.model.response.WalletResponseDTO;
import com.aquariux.trade.repository.UserWalletRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

  @Mock
  private UserWalletRepository userWalletRepository;
  @InjectMocks
  private WalletService walletService;
  private static final String userId = "ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff";

  @Test
  void testGetWalletBalance_with_existingData_success() {
    List<UserWalletEntity> wallets = Arrays.asList(
        new UserWalletEntity(UUID.randomUUID(), userId, Currency.USDT, new BigDecimal("78749.57")),
        new UserWalletEntity(UUID.randomUUID(), userId, Currency.ETH, new BigDecimal("77.79"))
    );
    when(userWalletRepository.findByUserId(userId)).thenReturn(wallets);

    WalletResponseDTO response = walletService.getWalletBalance(userId);

    assertEquals(userId, response.getUserId());
    assertEquals(3, response.getBalances().size());
    assertEquals(Currency.USDT, response.getBalances().get(0).getCurrency());
    assertEquals(new BigDecimal("78749.57"), response.getBalances().get(0).getBalance());
    assertEquals(Currency.ETH, response.getBalances().get(1).getCurrency());
    assertEquals(new BigDecimal("77.79"), response.getBalances().get(1).getBalance());
    assertEquals(Currency.BTC, response.getBalances().get(2).getCurrency());
    assertEquals(new BigDecimal("0.00"), response.getBalances().get(2).getBalance());
    verify(userWalletRepository, times(1)).findByUserId(userId);
    verify(userWalletRepository, never()).saveAll(any());
  }

  @Test
  void testGetWalletBalance_with_initializeNewUser_success() {
    when(userWalletRepository.findByUserId(userId)).thenReturn(Collections.emptyList())
        .thenReturn(Arrays.asList(
            new UserWalletEntity(UUID.randomUUID(), userId, Currency.USDT,
                new BigDecimal("50007.0")),
            new UserWalletEntity(UUID.randomUUID(), userId, Currency.ETH, new BigDecimal("0.0")),
            new UserWalletEntity(UUID.randomUUID(), userId, Currency.BTC, new BigDecimal("0.0"))
        ));

    WalletResponseDTO response = walletService.getWalletBalance(userId);

    assertEquals(userId, response.getUserId());
    assertEquals(3, response.getBalances().size());
    assertEquals(Currency.USDT, response.getBalances().get(0).getCurrency());
    assertEquals(new BigDecimal("50007.0"), response.getBalances().get(0).getBalance());
    assertEquals(Currency.ETH, response.getBalances().get(1).getCurrency());
    assertEquals(new BigDecimal("0.0"), response.getBalances().get(1).getBalance());
    assertEquals(Currency.BTC, response.getBalances().get(2).getCurrency());
    assertEquals(new BigDecimal("0.0"), response.getBalances().get(2).getBalance());
    verify(userWalletRepository, times(1)).saveAll(any());
  }
}