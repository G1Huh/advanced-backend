package com.lion.demo.chatting;
import com.lion.demo.entity.User;

import java.util.HashSet;
import java.util.List;
public interface RecipientService {
    List<Recipient> getFriendList(String uid);
    void insertFriend(User user, User friend);
}