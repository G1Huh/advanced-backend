package com.lion.demo.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 포스트 요청을 낚아채서 로그인을 진행시킴

// 로컬 로그인 - UserDetails 구현
// 소셜 로그인 -   구현
public class MyUserDetails implements UserDetails {
    // 로컬 로그인
    private User user;

    public MyUserDetails(){

    }
    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                System.out.println("getAuthority(): " + user.getRole());
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }
}
