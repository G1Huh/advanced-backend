package com.lion.demo.chatting;

import com.lion.demo.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long cmid;  //chat message id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "senderUid", referencedColumnName = "uid")
    private User sender;    // 보내는 사람
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipientUid", referencedColumnName = "uid")
    private User recipient; // 받는 사람
    
    private String message; // 메시지 내용
    private LocalDateTime timestamp;    // 메시지 발송 시간
    private int hasRead; // 읽음 여부 표시
}
