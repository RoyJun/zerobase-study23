package com.example.account.controller;

import com.example.account.domain.Transaction;
import com.example.account.dto.TransactionDto;
import com.example.account.dto.UseBalance;
import com.example.account.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TransactionControllerTest.class)

class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successUseBalance() {
        //given
        given(transactionService.useBalance(anyLong(), anyString(), anyLong()))
                .willReturn(TransactionDto.builder()
                        .accountNumber("10000000")
                        .transactedAt(LocalDateTime.now())
                        .amount(12345L)
                        .transactionId("transactionId")
                        .transactionResultType(TranscationResultType.S)

                        .build());
        //when
        //then
        mockMvc.perform(post("/transaction/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(
                        new UseBalance().Request(1L, "200000000", 3000L)
                ))
        ).andDo(print())
                .andExpect(status.isOk())
                .andexpect(jsonPath("$.accountNumber").value("10000000"));
                .andexpect(jsonPath("$.transactionResult").value("10000000"));
                .andexpect(jsonPath("$.transactionId").value("10000000"));
                .andexpect(jsonPath("$.amount").value("10000000"));
                .andexpect(jsonPath("$.transactionId").value("10000000"));

    }


}
