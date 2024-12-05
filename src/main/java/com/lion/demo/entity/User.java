package com.lion.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id // primary key
    private String uid;
    private String pwd;
    private String uname;
    private String email;
    private LocalDate regDate;
    private String role;    // 권한 (admin , user)
    private String provider; // 로그인 provider (소셜로그인)
    private String profileUrl;  // 프로필 이미지 url
}
