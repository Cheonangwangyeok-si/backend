package com.likelion.mindiary.domain.refreshToken.Repository;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.refreshToken.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByAccount(Account account);


}
