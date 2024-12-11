package com.lion.demo.chatting;

import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;


public interface ChatMessageService {
    List<ChatMessage> getListByUser(String uid, String friendid);
    ChatMessage getLastChatMessage(String uid, String friendUid);
    // 채팅방에서 채팅내역 불러오기(날짜별)
    Map<String, List<ChatMessage>> getChatMessagesByDate(String uid, String friendUid);

    int getNewCount(String friendUid, String uid);  // 내가 안 읽은 메시지

    ChatMessage insertChatMessage(ChatMessage chatMessage);
}