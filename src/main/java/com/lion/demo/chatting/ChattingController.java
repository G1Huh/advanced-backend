package com.lion.demo.chatting;

import com.lion.demo.entity.User;
import com.lion.demo.service.UserService;
import com.lion.demo.util.TimeUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/chatting")
public class ChattingController {
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private RecipientService recipientService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChattingWebSocketHandler webSocketHandler;
    @Autowired
    private TimeUtil timeUtil;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.ip}")
    private String serverIp;

    @GetMapping("/home")    // home -> 채팅방 리스트 페이지
    public String home(HttpSession session, Model model) {
        String sessUid = (String) session.getAttribute("sessUid");
        session.setAttribute("chattingStatus", "home");
        User user = userService.findByUid(sessUid);
        model.addAttribute("user", user);
        session.setAttribute("menu", "chatting");   // 메뉴 -> 채팅
        session.setAttribute("serverPort", serverPort);
        session.setAttribute("serverIp", serverIp);

        return "chatting/home";
    }

    @GetMapping("/getChatterList")
    @ResponseBody   // json 형식으로 응답
    public ResponseEntity<List<Chatter>> getChatterList(@RequestParam String userId) {
        List<Recipient> friendList = recipientService.getFriendList(userId);
        System.out.println("############# Friend LIst ##############\n" + friendList);
        List<Chatter> chatterList = new ArrayList<>();

        for (Recipient recipient : friendList) {

            User friend = recipient.getUser().getUid().equals(userId) ? recipient.getFriend() : recipient.getUser();
            // 마지막 메시지
            ChatMessage chatMessage = chatMessageService.getLastChatMessage(userId, friend.getUid());
            // 내가 안 읽은 메시지 수
            int newCount = chatMessageService.getNewCount(friend.getUid(), userId);
            Chatter chatter = Chatter.builder()
                    .friendUid(friend.getUid())
                    .friendUname(friend.getUname())
                    .friendProfileUrl(friend.getProfileUrl())
                    .message(chatMessage.getMessage())
                    .timeStr(TimeUtil.timeAgo(chatMessage.getTimestamp()))
                    .newCount(newCount)
                    .build();

            chatterList.add(chatter);
        }

        return ResponseEntity.ok(chatterList);
    }

    @PostMapping("/addFriend")
    @ResponseBody
    public String addFriend(String friendUid, HttpSession session) {
        String sessUid = (String) session.getAttribute("sessUid");

        User user = userService.findByUid(sessUid);
        User friend = userService.findByUid(friendUid);

        recipientService.insertFriend(user, friend);

        return "";
    }

    @GetMapping("/chat/{uid}")
    public String chat(@PathVariable String uid, HttpSession session, Model model) {
        String sessUid = (String) session.getAttribute("sessUid");
        User user = userService.findByUid(sessUid);
        User friend = userService.findByUid(uid);

        model.addAttribute("user", user);
        model.addAttribute("friend", friend);
        return "chatting/chat";
    }

    @GetMapping("/getChatItems")
    @ResponseBody
    public ResponseEntity<Map<String, List<ChatItem>>> getChatItems(
            @RequestParam("userId") String userId, @RequestParam("recipientId") String recipientId
    ) {
        User user = userService.findByUid(userId);
        User friend = userService.findByUid(recipientId);
        Map<String, List<ChatMessage>> map = chatMessageService.getChatMessagesByDate(userId, recipientId);

        Map<String, List<ChatItem>> chatItemsByDate = new LinkedHashMap<>();
        for (Map.Entry<String, List<ChatMessage>> entry: map.entrySet()) {
            String key = entry.getKey();
            List<ChatItem> list = new ArrayList<>();
            for (ChatMessage cm: map.get(key)) {
                ChatItem chatItem = ChatItem.builder()
                        .isMine(cm.getSender().getUid().equals(userId) ? 1 : 0)
                        .message(cm.getMessage())
                        .timeStr(timeUtil.amPmStr(cm.getTimestamp()))
                        .hasRead(cm.getHasRead())
                        .friendUname(friend.getUname())
                        .friendProfileUrl(friend.getProfileUrl())
                        .build();
                list.add(chatItem);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN);
            String date = LocalDate.parse(key).format(formatter);
            chatItemsByDate.put(date, list);
        }
        return ResponseEntity.ok(chatItemsByDate);
    }

    @PostMapping("/insert")
    @ResponseBody
    public String insert(String senderUid, String recipientUid, String message){
        User sender = userService.findByUid(senderUid);
        User recipient = userService.findByUid(recipientUid);
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .timestamp(LocalDateTime.now())
                .hasRead(webSocketHandler.isReadable(senderUid, recipientUid))
                .build();

        chatMessageService.insertChatMessage(chatMessage);
        return "ok";
    }

    @GetMapping("/mock")
    public String mockForm() {
        return "chatting/mock";
    }

    @PostMapping("/mock")
    public String mockProc(String senderUid, String recipientUid, String message, LocalDateTime timestamp) {
        User sender = userService.findByUid(senderUid);
        User recipient = userService.findByUid(recipientUid);
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .message(message)
                .timestamp(timestamp)
                .hasRead(0)
                .build();
        System.out.println(chatMessage);
        chatMessageService.insertChatMessage(chatMessage);
        recipientService.insertFriend(sender, recipient);

        return "redirect:/chatting/mock";
    }
}