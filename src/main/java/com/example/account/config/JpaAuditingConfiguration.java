package com.example.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // 자동 Bean 으로 등록
@EnableJpaAuditing //Jpa 어디팅이 켜진 상태로 어플리케이션 실행
public class JpaAuditingConfiguration {
}
