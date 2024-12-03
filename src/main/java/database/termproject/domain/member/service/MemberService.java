package database.termproject.domain.member.service;


import database.termproject.domain.member.dto.request.MemberEditRequest;
import database.termproject.domain.member.dto.request.MemberEmailVerifyRequest;
import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.member.dto.response.MemberResponse;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.entity.MemberProfile;
import database.termproject.domain.member.repository.MemberProfileRepository;
import database.termproject.domain.member.repository.MemberRepository;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static database.termproject.global.error.ProjectError.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;


    public MemberResponse getMemberInfo(){
        Member member = getMember();
        MemberResponse memberResponse = MemberResponse.from(member);
        return memberResponse;
    }

    @Transactional
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

        MemberProfile memberProfile = MemberProfile.builder()
                .name(memberSignUpRequestDto.name())
                .address(memberSignUpRequestDto.address())
                .addressDetail(memberSignUpRequestDto.addressDetail())
                .phoneNumber(memberSignUpRequestDto.phoneNumber())
                .build();

        member.setMemberProfile(memberProfile);

        memberProfileRepository.save(memberProfile);
        memberRepository.save(member);
    }

    @Transactional
    public MemberResponse editMemberInfo(MemberEditRequest memberEditRequest){

        Member member = memberRepository.findByIdWithProfile(getMember().getId())
                .orElseThrow(() -> new ProjectException(MEMBER_NOT_FOUND));

        MemberProfile memberProfile = member.getMemberProfile();

        System.out.println("정보가져옴 --------");

        String password = null;
        if(memberEditRequest.password() != null){
            password = passwordEncoder.encode(memberEditRequest.password());
        }

        member.editMemberInfo(
                password
        );

        System.out.println("멤버수정 --------");

        memberProfile.editMemberProfile(memberEditRequest.name(),
                memberEditRequest.address(),
                memberEditRequest.addressDetail(),
                memberEditRequest.phoneNumber());

        System.out.println("멤버profile수정 --------");

        memberProfileRepository.save(memberProfile);



        System.out.println("profile save~");

        memberRepository.save(member);

        System.out.println("member save~");


        return MemberResponse.from(member);
    }

    public MemberResponse softDeleteMember(){
        Member member = getMember();
        member.deleteMember();
        memberRepository.save(member);
        return MemberResponse.from(member);
    }



    @Transactional
    public void generateAuthCode(){
        Member member = getMember();

        if(member.isVerifyMember()){
            throw new ProjectException(ALREADY_VERIFY_MEMBER);
        }


        /*
        * TODO
        *  1. 메일 보내기
        *  2. 5분 스케줄링 (만료)
        * */

    }

    public void verifyAuthCode(MemberEmailVerifyRequest memberEmailVerifyRequest){
        Member member = getMember();
        String emailCode = memberEmailVerifyRequest.code();


        //TODO : if 멤버의 이메일코드가 맞다면 등업

        //아니라면 memberProfile 삭제 후

    }



    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ProjectException(MEMBER_NOT_FOUND));
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }
}
