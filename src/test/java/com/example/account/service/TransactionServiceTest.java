package com.example.account.service;


import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.TransactionDto;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @Test
    void successUseBalance() {
        //given
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("Pobi")
                .build();
        given(accountRepository.findById(anyLong()))
                .willReturn((Optional<Account>) Optional.of(user));

        given(accountRepository.findByAccountNumber(anyString()))
                .willReturn(Optional.of(Account.builder()
                                .accountUser(user)
                                .balance(0L)
                                .accountNumber("100000012")
                        .build()));


        //when
        TransactionDto transactionDto = transactionService.useBalance(1L, "1000000000", 1000L);
        //then
        assertEquals(9000L, transactionDto.getBalanceSnapShot());

    }




}
