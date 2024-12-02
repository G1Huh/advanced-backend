package com.lion.demo.service;

import com.lion.demo.entity.User;

import java.util.List;

public interface UserService {
    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int USER_NOT_EXIST = 2;
    User findByUid(String uid);
    List<User> getUsers();
    void registerUser(User user);
    void updateUser(User user);
    void deleteUser(String uid);
    int login(String uid, String pwd);
}
