package com.lion.demo.chatting;

import com.lion.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecipientServiceImpl implements RecipientService {
    @Autowired
    private RecipientRepository recipientRepository;

    @Override
    public List<Recipient> getFriendList(String uid) {
        // list 1 -> 내가 보낸 것 list
        List<Recipient> list1 = recipientRepository.findByUserUid(uid);
        System.out.println("&&&&&&&&&&& list1 &&&&&& \n" + list1);
        // list 2 -> 내가 받은 것 list
        List<Recipient> list2 = recipientRepository.findByFriendUid(uid);
        System.out.println("&&&&&&&&&&& list2 &&&&&& \n" + list2);

        List<Recipient> mergedList = Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparing(Recipient::getTimestamp))
                .collect(Collectors.toList());




            return mergedList;
    }

    @Override
    public void insertFriend(User user, User friend) {
        Recipient recipient = Recipient.builder()
                .user(user)
                .friend(friend)
                .timestamp(LocalDateTime.now())
                .build();
        recipientRepository.save(recipient);

    }
}
