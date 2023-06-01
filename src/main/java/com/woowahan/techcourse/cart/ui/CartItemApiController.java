package com.woowahan.techcourse.cart.ui;

import com.woowahan.techcourse.cart.application.CartCommandService;
import com.woowahan.techcourse.cart.application.CartQueryService;
import com.woowahan.techcourse.cart.dto.CartItemQuantityUpdateRequest;
import com.woowahan.techcourse.cart.dto.CartItemRequest;
import com.woowahan.techcourse.cart.dto.CartItemResponse;
import com.woowahan.techcourse.member.domain.Member;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-items")
public class CartItemApiController {

    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;

    public CartItemApiController(CartCommandService cartCommandService, CartQueryService cartQueryService) {
        this.cartCommandService = cartCommandService;
        this.cartQueryService = cartQueryService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> showCartItems(Member member) {
        return ResponseEntity.ok(cartQueryService.findByMember(member));
    }

    @PostMapping
    public ResponseEntity<Void> addCartItems(Member member, @RequestBody CartItemRequest cartItemRequest) {
        Long cartItemId = cartCommandService.add(member, cartItemRequest);

        return ResponseEntity.created(URI.create("/cart-items/" + cartItemId)).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCartItemQuantity(Member member, @PathVariable Long id,
            @RequestBody CartItemQuantityUpdateRequest request) {
        cartCommandService.updateQuantity(member, id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItems(Member member, @PathVariable Long id) {
        cartCommandService.remove(member, id);

        return ResponseEntity.noContent().build();
    }
}