package com.likelion.mindiary.domain.account.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column
    private String provider;

}
