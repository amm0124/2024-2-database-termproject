package database.termproject.domain.member.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_MANAGER,      // 중간 관리자
    ROLE_USER,         // 인증된 유저
    ROLE_ANONYMOUS     // 인증되지 않은 유저
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
