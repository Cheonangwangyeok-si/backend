package com.likelion.mindiary.domain.account.service;

import com.likelion.mindiary.domain.account.model.Account;
import com.likelion.mindiary.domain.account.model.AccountRole;
import com.likelion.mindiary.domain.account.repository.AccountRepository;
import com.likelion.mindiary.global.Security.CustomOauth2UserDetails;
import com.likelion.mindiary.global.Security.GoogleUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;


    // 소셜 로그인
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        GoogleUserDetails googleUserDetails = new GoogleUserDetails(oAuth2User.getAttributes());

        String loginId = googleUserDetails.getProviderId();
        String name = googleUserDetails.getName();

        Account findMember = accountRepository.findByLoginId(loginId);

        Account member;

        if (findMember == null) {
            member = Account.builder()
                    .loginId(loginId)
                    .name(name)
                    .provider(provider)
                    .role(AccountRole.USER)
                    .build();
            accountRepository.save(member);

        } else {
            member = findMember;
        }

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());
    }

}
