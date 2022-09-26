package com.example.account.domain;

import com.example.account.exception.AccountException;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class) //자동으로 날짜 저장 config 전체 설정필요
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AccountUser accountUser;
    private String accountNumber;

    @Enumerated(EnumType.STRING) // DB 에 0,1,2,3 과 같은 인덱스값으로 저장 안하게 하기 위해서
    private AccountStatus accountStatus;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;

    @CreatedDate //자동으로 날짜 저장
    private LocalDateTime createAt;
    @LastModifiedDate //자동으로 날짜 저장
    private LocalDateTime updateAt;

    public void useBalance(Long amount) {
        if (amount > balance){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);

        }
        balance -= amount;
    }

    public void cancelBalance(Long amount) {
        if (amount < 0){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);

        }
        balance -= amount;
    }
}
