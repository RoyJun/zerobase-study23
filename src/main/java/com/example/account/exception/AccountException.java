package com.example.account.exception;

import com.example.account.type.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    // 그냥 exception하면 checkedException 되니까 뒤에 런타임 익셉션 익스텐즈 해줌
    // 요즘 체크드익셉션 잘 안씀

    private ErrorCode errorCode;
    private String errorMessage;

    public AccountException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}
