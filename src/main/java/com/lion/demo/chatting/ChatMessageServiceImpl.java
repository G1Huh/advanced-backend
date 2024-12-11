package com.lion.demo.chatting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatMessage> getListByUser(String uid, String friendid) {
        return null;
    }

    @Override
    public ChatMessage getLastChatMessage(String uid, String friendUid) {
        // timestamp 내림차순으로 chatting message list 불러오기
        List<ChatMessage> list1 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(uid, friendUid);
        List<ChatMessage> list2 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(friendUid, uid);

        if (list1.isEmpty() && list2.isEmpty())
            return ChatMessage.builder().message("").timestamp(LocalDateTime.now()).build();

        if (list1.isEmpty())
            return list2.get(0);
        if (list2.isEmpty())
            return list1.get(0);

        // 양 쪽 모두 리스트가 빈 값이 아니면 (메시지 내역이 있으면) 후시에 온 chat message 반환
        return list1.get(0).getTimestamp().isAfter(list2.get(0).getTimestamp()) ? list1.get(0) : list2.get(0);
    }

    @Override
    public Map<String, List<ChatMessage>> getChatMessagesByDate(String uid, String friendUid) {
        // timestamp 내림차순으로 chatting message list 불러오기
        List<ChatMessage> list1 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(uid, friendUid);
        List<ChatMessage> list2 = chatMessageRepository.findBySenderUidAndRecipientUidOrderByTimestampDesc(friendUid, uid);

        if (list2 != null) {
            for (ChatMessage cm : list2) {
                if (cm.getHasRead() == 0) {
                    // 현재 화면에 메시지 읽음 상태로 표시
                    cm.setHasRead(1);
                    // DB에 메시지를 읽음 상태로 update
                    chatMessageRepository.updateHasRead(cm.getCmid());
                }
            }
        }
        List<ChatMessage> mergedList = Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                .collect(Collectors.toList());

        // LinkedHashMap --> 순서 보장
        Map<String, List<ChatMessage>> map = new LinkedHashMap<>();
        for (ChatMessage cm : mergedList) {
            // 날짜 (ex. 2024-12-11)
            String date = cm.getTimestamp().toString().substring(0, 10);
            if (map.containsKey(date)) {
                // 해당 날짜 채팅 데이터가 있을 때
                List<ChatMessage> cmList = map.get(date);
                // chatMessageList 에 현재 chatItem 추가하고 map 업데이트
                cmList.add(cm);
                map.replace(date, cmList);
            } else {
                // 해당 날짜 채팅 데이터가 없을 때
                List<ChatMessage> cmList = new ArrayList<>();
                cmList.add(cm);
                map.put(date, cmList);
            }
        }


        return map;
    }

    @Override   // 내가 안 읽은 메시지 수
    public int getNewCount(String friendUid, String uid) {
        Integer newCount = chatMessageRepository.getNewCount(friendUid, uid);
        return (newCount != null) ? newCount : 0;
    }

    @Override
    public ChatMessage insertChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
