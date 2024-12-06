package com.lion.demo.service;

import com.lion.demo.entity.Book;
import com.lion.demo.entity.Cart;
import com.lion.demo.entity.User;
import com.lion.demo.repository.BookRepository;
import com.lion.demo.repository.CartRepository;
import com.lion.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartServiceImpl cartService;    // CartServiceImpl 객체 생성하고, Mock객체(Repository) 주입

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartItemsByser(){
        // Given
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        when(cartRepository.findByUserUid("uid")).thenReturn(Arrays.asList(cart1, cart2));

        // When
        List<Cart> cartList = cartService.getCartItemsByUser("uid");

        // Then
        assertThat(cartList).hasSize(2);
    }

    @Test
    void testAddToCart(){
        User user = User.builder().uid("uid").build();
        Book book = Book.builder().bid(999L).build();
        Cart cart = Cart.builder().user(user).book(book).quantity(3).build();

        when(userRepository.findById("uid")).thenReturn(Optional.of(user));
        when(bookRepository.findById(999L)).thenReturn(Optional.of(book));

        cartService.addToCart("uid", 999L, 3);

        verify(cartRepository, times(1)).save(cart);
    }

}
