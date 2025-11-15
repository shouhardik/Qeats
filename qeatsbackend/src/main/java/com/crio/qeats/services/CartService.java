package com.crio.qeats.services;

import com.crio.qeats.exchanges.AddCartItemRequest;
import com.crio.qeats.exchanges.ClearCartRequest;
import com.crio.qeats.exchanges.GetCartResponse;

public interface CartService {
  
  GetCartResponse findCartByUserId(String userId);
  
  GetCartResponse addItemToCart(AddCartItemRequest addCartItemRequest, String userId);
  
  GetCartResponse removeItemFromCart(AddCartItemRequest removeCartItemRequest, String userId);
  
  GetCartResponse clearCart(ClearCartRequest clearCartRequest, String userId);
  
}

