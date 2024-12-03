package database.termproject.domain.posting.entity;

import database.termproject.domain.member.entity.Member;
import database.termproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(indexes = {
        @Index(name = "idx_member", columnList = "member_id"),
        @Index(name = "idx_posting_type", columnList = "postingType")
})
@Getter
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE posting SET is_deleted = true where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //이거 왜 없으면 빨간 줄? posting
public class Posting extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id") // 외래 키 컬럼 이름 명시
    private Member member;

    private String title;

    private String game;

    private String content;

    @Enumerated(EnumType.STRING) // Enum이 문자열로 저장되도록 설정
    private PostingType postingType;

    @Builder
    public Posting(Member member, String title, String game, String content, PostingType postingType) {
        this.member = member;
        this.title = title;
        this.game = game;
        this.content = content;
        this.postingType = postingType;
    }

}
