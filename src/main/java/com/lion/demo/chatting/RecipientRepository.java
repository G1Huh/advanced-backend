package com.lion.demo.chatting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    List<Recipient> findByUserUid(String uid);
    List<Recipient> findByFriendUid(String friendUid);

    // 친구 목록
    @Query("SELECT replace(concat(r.friend.uid, r.user.uid), :uid, '') as chatter from Recipient r where r.friend.uid=:uid or r.user.uid=:uid group by chatter")
    List<String> getFriends(@Param("uid")String uid);
}
