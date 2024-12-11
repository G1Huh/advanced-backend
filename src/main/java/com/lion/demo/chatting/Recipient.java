package com.lion.demo.chatting;

import com.lion.demo.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;   // recipient id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friendUid", referencedColumnName = "uid")
    private User friend;
    private LocalDateTime timestamp;

}
