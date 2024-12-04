package database.termproject.domain.facilities.service;

import database.termproject.domain.facilities.dto.request.FacilitiesRegisterRequest;
import database.termproject.domain.facilities.dto.response.FacilitiesResponse;
import database.termproject.domain.facilities.entity.Facilities;
import database.termproject.domain.facilities.repository.FacilitiesRepository;
import database.termproject.domain.member.entity.Member;
import database.termproject.global.error.ProjectError;
import database.termproject.global.error.ProjectException;
import database.termproject.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilitiesService {

    private final FacilitiesRepository facilitiesRepository;


    @Transactional
    public FacilitiesResponse register(FacilitiesRegisterRequest facilitiesRegisterRequest) {
        Member member = getMember();

        Facilities facilities = Facilities.builder()
                .member(member)
                .storeName(facilitiesRegisterRequest.storeName())
                .address(facilitiesRegisterRequest.address())
                .addressDetail(facilitiesRegisterRequest.addressDetail())
                .phone(member.getMemberProfile().getPhoneNumber())
                .build();

        facilitiesRepository.save(facilities);
        return FacilitiesResponse.from(facilities);
    }

    @Transactional(readOnly = true)
    public FacilitiesResponse getFacilities() {
        Long memberId = getMember().getId();
        Facilities facilities = facilitiesRepository.findByMemberId(memberId)
                .orElseThrow( () -> new ProjectException(ProjectError.FACILITIES_NOT_FOUND));
        return FacilitiesResponse.from(facilities);
    }

    @Transactional
    public FacilitiesResponse editFacilities(FacilitiesRegisterRequest facilitiesRegisterRequest){
        Long memberId = getMember().getId();
        Facilities facilities = facilitiesRepository.findByMemberId(memberId)
                .orElseThrow( () -> new ProjectException(ProjectError.FACILITIES_NOT_FOUND));
        facilities.edit(facilitiesRegisterRequest.storeName(),
                facilitiesRegisterRequest.address(),
                facilitiesRegisterRequest.addressDetail(),
                facilities.getPhone());

        return FacilitiesResponse.from(facilities);
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        Object impl = authentication.getPrincipal();
        Member member = ((UserDetailsImpl) impl).getMember();
        return member;
    }





}
