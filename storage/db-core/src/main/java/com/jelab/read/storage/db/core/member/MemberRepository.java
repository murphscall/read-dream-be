package com.jelab.read.storage.db.core.member;

import com.jelab.read.core.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialIdAndSocialType(String socialId, SocialType socialType);

}
