package database.termproject.admin.service;


import database.termproject.admin.dto.request.ManagerRegisterRequest;
import database.termproject.domain.facilities.dto.request.FacilitiesRegisterRequest;
import database.termproject.domain.facilities.entity.Facilities;
import database.termproject.domain.facilities.service.FacilitiesService;
import database.termproject.domain.member.dto.request.MemberSignUpRequestDto;
import database.termproject.domain.member.entity.Member;
import database.termproject.domain.member.service.MemberService;
import database.termproject.domain.posting._comment.entity.Comment;
import database.termproject.domain.posting._comment.service.CommentService;
import database.termproject.domain.posting.entity.Posting;
import database.termproject.domain.posting.service.PostingServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static database.termproject.domain.member.entity.Role.ROLE_ADMIN;
import static database.termproject.domain.member.entity.Role.ROLE_MANAGER;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PostingServiceImpl postingService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final FacilitiesService facilitiesService;

    @Transactional
    public void deleteMemberByAdmin(Long memberId){
        Member member = memberService.getMemberById(memberId);
        member.softDelete();
    }


    @Transactional
    public void deletePostingByAdmin(Long postingId){
        Posting posting = postingService.getPostingByPostingId(postingId);
        posting.softDelete();
    }

    @Transactional
    public void restorePostingByAdmin(Long postingId){
        Posting posting = postingService.getDeletePostingByPostingId(postingId);
        posting.restore();
    }

    @Transactional
    public void deleteCommentByAdmin(Long commentId){
        Comment comment = commentService.getCommentByCommentId(commentId);
        comment.softDelete();
    }


    @Transactional
    public void restoreCommentByAdmin(Long commentId){
        Comment comment = commentService.getDeleteCommentByCommentId(commentId);
        comment.restoreComment();
    }


    @Transactional
    public void registerManager(ManagerRegisterRequest managerRegisterRequest){

        Member member = memberService.signUp(new MemberSignUpRequestDto(
                managerRegisterRequest.email(),
                managerRegisterRequest.password(),
                managerRegisterRequest.name(),
                managerRegisterRequest.memberAddress(),
                managerRegisterRequest.memberAddressDetail(),
                managerRegisterRequest.phoneNumber()),
                ROLE_MANAGER
        );

        Facilities facilities = Facilities.builder()
                .member(member)
                .storeName(managerRegisterRequest.storeName())
                .address(managerRegisterRequest.address())
                .addressDetail(managerRegisterRequest.addressDetail())
                .phone(member.getMemberProfile().getPhoneNumber())
                .build();
        facilitiesService.save(facilities);
    }

    @Transactional
    public void registerAdmin(MemberSignUpRequestDto memberSignUpRequestDto){
        memberService.signUp(memberSignUpRequestDto, ROLE_ADMIN);
    }

    @Transactional
    public void restoreMemberByAdmin(Long memberId){
        Member member = memberService.getMemberById(memberId);
        member.restore();
    }

}
