package database.termproject.domain.member.service;


import database.termproject.domain.verifyemail.entity.EmailVerification;
import database.termproject.domain.verifyemail.service.EmailService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static database.termproject.global.error.ProjectError.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final EmailService emailService;

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

        String password = null;
        if(memberEditRequest.password() != null){
            password = passwordEncoder.encode(memberEditRequest.password());
        }

        member.editMemberInfo(
                password
        );

        memberProfile.editMemberProfile(memberEditRequest.name(),
                memberEditRequest.address(),
                memberEditRequest.addressDetail(),
                memberEditRequest.phoneNumber());

        memberProfileRepository.save(memberProfile);
        memberRepository.save(member);

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

        String memberEmail = member.getEmail();
        if(member.isVerifyMember()){
            throw new ProjectException(ALREADY_VERIFY_MEMBER);
        }

        String title = "2024 데이터베이스 텀프로젝트 이메일 인증 번호";

        String authCode = this.createCode();

        String text = "안녕하세요. " + member.getMemberProfile().getName()+"님 \n"+ "인증 코드는 "+authCode+"입니다.";

        emailService.sendEmail(memberEmail, title, text);
        emailService.saveEmailVerification(EmailVerification
                .builder()
                .member(member)
                .code(authCode)
                .build()
        );

    }

    @Transactional
    public void verifyAuthCode(MemberEmailVerifyRequest memberEmailVerifyRequest){

        Member member = memberRepository.findByIdWithProfile(getMember().getId())
                .orElseThrow(() -> new ProjectException(MEMBER_NOT_FOUND));

        String emailVerificationCode = memberEmailVerifyRequest.code();
        EmailVerification emailVerification = emailService.getEmailVerification(member.getId(), emailVerificationCode);

        if(member.isVerifyMember()){
            throw new ProjectException(ALREADY_VERIFY_MEMBER);
        }

        if(emailVerification.isExpired()){
            throw new ProjectException(CODE_EXPIRED_EXCEPTION);
        }

        emailService.removeEmailVerification(emailVerification);
        member.convertRoleUser();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ProjectException(MEMBER_NOT_FOUND));
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new ProjectException(MAIL_CODE_GENERATING_EXCEPTION);
        }
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }
}
