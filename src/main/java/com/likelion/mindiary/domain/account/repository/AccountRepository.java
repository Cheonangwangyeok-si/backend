package com.likelion.mindiary.domain.account.repository;

import com.likelion.mindiary.domain.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByLoginId(String loginId);

}
