package database.termproject.global.security;

import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.repository.MemberRepository;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    public UserDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ProjectException(ProjectError.MEMBER_NOT_FOUND));

        //user details에 담아서 return 하면 -> authentication manager가 인증
        return new UserDetailsImpl(member);
    }

}