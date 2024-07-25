package com.likelion.mindiary.domain.refreshToken.model;


import com.likelion.mindiary.domain.account.model.Account;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long token_id;

    @Column
    private String token;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
