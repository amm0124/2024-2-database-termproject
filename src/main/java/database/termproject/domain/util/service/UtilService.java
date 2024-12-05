package database.termproject.domain.util.service;


import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.entity.Role;
import database.termproject.domain.member.repository.MemberRepository;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final MemberRepository memberRepository;

    public void change(Role role){
        Member member = getMember();
        member.setRole(role);
        memberRepository.save(member);
    }


    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }

}
