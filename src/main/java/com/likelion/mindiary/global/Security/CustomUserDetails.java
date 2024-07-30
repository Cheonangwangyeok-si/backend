package com.likelion.mindiary.global.Security;


import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails {

    private final Account member;

    public CustomUserDetails(Account member) {
        this.member = member;
    }

    public String getProvidedId(){
        return member.getLoginId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getRole(){
        if(member.getRole().equals(AccountRole.USER)) return "USER";
        else return "ADMIN";
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
