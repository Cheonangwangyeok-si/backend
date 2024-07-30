package com.likelion.mindiary.domain.account.controller.dto.response;

import com.likelion.mindiary.domain.account.model.AccountRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String loginId;
    private String name;

}
