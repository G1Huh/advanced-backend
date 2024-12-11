package com.lion.demo.chatting;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderUidAndRecipientUid(String uid, String friendUid); // 보내는사람-받는사람 페어로 찾기

    List<ChatMessage> findBySenderUidAndRecipientUidOrderByTimestampDesc(String uid, String friendUid);

    // 내가 안 읽은 메시지 수 (JPQL)
    @Query("select count(c) - sum(c.hasRead) as newCount from ChatMessage c where c.sender.uid=:suid and c.recipient.uid=:ruid")
    Integer getNewCount(@Param("suid") String suid, @Param("ruid") String ruid);

    @Transactional
    @Modifying
    @Query("update ChatMessage c set c.hasRead=1 where c.cmid=:cmid")
    void updateHasRead(long cmid);


}
