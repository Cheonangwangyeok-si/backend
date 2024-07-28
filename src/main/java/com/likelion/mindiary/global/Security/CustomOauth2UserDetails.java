package com.likelion.mindiary.global.Security;


import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOauth2UserDetails implements UserDetails, OAuth2User {

    private final Account member;
    private Map<String, Object> attributes;

    public CustomOauth2UserDetails(Account member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }
    public  CustomOauth2UserDetails(Account member){
        this.member = member;
    }

    @Override
    public String getName() {
        return member.getName();
    }

    public String getProvidedId(){
        return (String) attributes.get("sub");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
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
