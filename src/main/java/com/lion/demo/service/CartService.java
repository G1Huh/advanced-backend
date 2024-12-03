package com.lion.demo.service;

import com.lion.demo.entity.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCartItemsByUser(String uid);
    void addToCart(String uid, long bid, int quantity);
    void removeFromCart(long cid);
    void clearCart(String uid);

}
