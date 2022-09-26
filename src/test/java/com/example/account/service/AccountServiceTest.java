package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import com.example.account.type.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccess () {
        //given
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(AccountUser.builder()
                        .name("Pobi").build()));
        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(Account.builder()
                        .accountUser(user)
                        .accountNumber("100000012").build()));
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000013").build());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 100000000L);


        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("100000013", captor.getValue().getAccountNumber());
    }

    @Test
    @DisplayName("유저 당 최대 계좌는 10개")
    void createAccount_maxAccountIs10() {
        //given
        AccountUser user = AccountUser.builder()
                .id(15L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(anyString()))
                .willReturn(10);

        //when
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));
        //then
        assertEquals(ErrorCode.MAX_ACCOUNT_PER_USER_10, exception.getErrorCode());
    }

        @Test
        @DisplayName("해지 계좌는 잔액이 없어야 한다.")
        void deleteAccountFailed_balanceNotEmpty() {
            //given
            AccountUser pobi = AccountUser.builder()
                    .id(12L)
                    .name("Pobi").build();
            given(accountUserRepository.findById(anyLong()))
                    .willReturn(Optional.of(pobi));
            given(accountRepository.findByAccountNumber(anyString()))
                    .willReturn(Optional.of(Account.builder()
                            .accountUser(pobi)
                            .balance(100L)
                            .accountNumber("1000000012").build()));

            //when
            AccountException exception = assertThrows(AccountException.class,
                    () -> accountService.deleteAccount(1L,"1234567890"));

            //then
            assertEquals(ErrorCode.BALANCE_NOT_EMPTY,exception.getErrorCode());

         }

         @Test
         @DisplayName("해지 계좌는 해지할 수 없다.")
         void deleteAccountFailed_alreadyUnregistered() {
             //given
             AccountUser pobi = AccountUser.builder()
                     .id(2L)
                     .name("Pobi").build();
             given(accountUserRepository.findById(anyLong()))
                     .willReturn(Optional.of(pobi));
             given(accountRepository.findByAccountNumber(anyString()))
                     .willReturn(Optional.of(Account.builder()
                             .accountUser(pobi)
                             .accountStatus(AccountStatus.UNREGISTERED)
                             .balance(0L)
                             .accountNumber("1000000012").build()));

             //when
             AccountException exception = assertThrows(AccountException.class,
                     () -> accountService.deleteAccount(1L,"1234567890"));

             //then
             assertEquals(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED,exception.getErrorCode());


    }
    @Test
    void successGetAccountsByUserId() {
        //given
        AccountUser pobi = AccountUser.builder()
                .id(2L)
                .name("Pobi").build();
        List<Account> accounts = Arrays.asList(
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("1111111111")
                        .balance(1000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("2222222222")
                        .balance(2000L)
                        .build(),
                Account.builder()
                        .accountUser(pobi)
                        .accountNumber("3333333333")
                        .balance(3000L)
                        .build()
        );
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(pobi));
        //then
        given(accountRepository.findByAccountNumber(any()))
                .willReturn(accounts);
        //when
        List<AccountDto> accountDtos = accountService.getAccountsByUserId(1l);

        //then
        assertEquals(3, accountDtos.size());
        assertEquals("1111111111", accountDtos.get(0).getAccountNumber());
        assertEquals(1000, accountDtos.get(0).getBalance());
        assertEquals("2222222222", accountDtos.get(1).getAccountNumber());
        assertEquals(2000, accountDtos.get(1).getBalance());
        assertEquals("3333333333", accountDtos.get(2).getAccountNumber());
        assertEquals(3000, accountDtos.get(2).getBalance());

    }
        @Test
        void failedToGetAccounts() {
            //given
            given(accountUserRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            AccountException exception = assertThrows(AccountException.class,
                    () -> accountService.getAccountsByUserId(1L);

            //then
            assertEquals(ErrorCode.USER_NOT_FOUND,exception.getErrorCode());







    }


}