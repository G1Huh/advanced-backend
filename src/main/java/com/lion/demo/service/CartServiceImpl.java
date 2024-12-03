package com.lion.demo.service;


import com.lion.demo.entity.Book;
import com.lion.demo.entity.Cart;
import com.lion.demo.entity.User;
import com.lion.demo.repository.BookRepository;
import com.lion.demo.repository.CartRepository;
import com.lion.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Cart> getCartItemsByUser(String uid) {
        return cartRepository.findByUserUid(uid);
    }

    @Override
    public void addToCart(String uid, long bid, int quantity) {
        User user = userRepository.findById(uid).orElse(null);
        Book book = bookRepository.findById(bid).orElse(null);
        if (user != null && book != null) {
            Cart cart = Cart.builder()
                    .user(user)
                    .book(book)
                    .quantity(quantity)
                    .build();

            cartRepository.save(cart);
        } else {
            throw new RuntimeException("User or Book Not Found");
        }
    }

    @Override
    public void removeFromCart(long cid) {
        cartRepository.deleteById(cid);
    }

    @Override
    public void clearCart(String uid) {
        List<Cart> cartList = cartRepository.findByUserUid(uid);
        cartRepository.deleteAll(cartList);
    }
}
