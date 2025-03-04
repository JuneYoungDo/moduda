package com.korean.moduda.global.security.auth;

import com.korean.moduda.domain.member.Member;
import com.korean.moduda.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByIdWithDeleted(Long.parseLong(username))
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with memberId: " + username));
        return new PrincipalDetails(memberEntity);
    }
}
