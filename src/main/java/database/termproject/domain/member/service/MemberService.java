package database.termproject.domain.member.service;


import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.repository.MemberRepository;
import database.termproject.global.error.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static database.termproject.global.error.ProjectError.EXIST_MEMBER;
import static database.termproject.global.error.ProjectError.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void signUp(MemberSignUpRequestDto memberSignUpRequestDto) {
        String email = memberSignUpRequestDto.email();
        String password = memberSignUpRequestDto.password();

        Optional<Member> exist = memberRepository.findByEmail(email);
        if (exist.isPresent()) {
            throw new ProjectException(EXIST_MEMBER);
        }

        password = passwordEncoder.encode(password);

        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ProjectException(MEMBER_NOT_FOUND));
    }

}
