package com.jelab.read.storage.db.core.member;

import com.jelab.read.core.enums.SocialType;
import com.jelab.read.storage.db.core.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SOCIAL_ID_TYPE",
                        columnNames = {"socialId", "social_type"}
                )
        }
)
public class Member extends BaseEntity {

    @Column(name = "social_id", nullable = false)
    private String socialId;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    protected Member() {
    }

    private Member(String socialId, String email, String name, SocialType socialType) {
        this.socialId = socialId;
        this.email = email;
        this.name = name;
        this.socialType = socialType;
    }

    public static Member createSocialMember(String sub, String email, String name, SocialType socialType) {
        return new Member(sub, email, name, socialType);
    }

    public String getSocialId() {
        return socialId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
