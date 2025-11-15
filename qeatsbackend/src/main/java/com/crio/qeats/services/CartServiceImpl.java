package com.crio.qeats.services;

import com.crio.qeats.dto.Cart;
import com.crio.qeats.dto.Item;
import com.crio.qeats.exchanges.AddCartItemRequest;
import com.crio.qeats.exchanges.ClearCartRequest;
import com.crio.qeats.exchanges.GetCartResponse;
import com.crio.qeats.models.CartEntity;
import com.crio.qeats.models.ItemEntity;
import com.crio.qeats.models.MenuEntity;
import com.crio.qeats.repositories.CartRepository;
import com.crio.qeats.repositories.MenuRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Provider;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  private static final String LIVE_STATUS = "LIVE";

  @Override
  public GetCartResponse findCartByUserId(String userId) {
    Optional<CartEntity> cartEntityOptional = cartRepository.findByUserIdAndStatus(userId, LIVE_STATUS);
    
    if (cartEntityOptional.isEmpty()) {
      return new GetCartResponse(null);
    }
    
    CartEntity cartEntity = cartEntityOptional.get();
    Cart cart = convertToCart(cartEntity);
    return new GetCartResponse(cart);
  }

  @Override
  public GetCartResponse addItemToCart(AddCartItemRequest request, String userId) {
    Optional<MenuEntity> menuOptional = menuRepository.findByRestaurantId(request.getRestaurantId());
    
    if (menuOptional.isEmpty()) {
      throw new RuntimeException("Menu not found for restaurant: " + request.getRestaurantId());
    }
    
    MenuEntity menu = menuOptional.get();
    Optional<ItemEntity> itemOptional = menu.getItems().stream()
        .filter(item -> item.getItemId().equals(request.getItemId()))
        .findFirst();
    
    if (itemOptional.isEmpty()) {
      throw new RuntimeException("Item not found in restaurant menu");
    }
    
    ItemEntity itemToAdd = itemOptional.get();
    
    Optional<CartEntity> cartEntityOptional = cartRepository.findByUserIdAndStatus(userId, LIVE_STATUS);
    
    CartEntity cartEntity;
    if (cartEntityOptional.isEmpty()) {
      cartEntity = new CartEntity();
      cartEntity.setUserId(userId);
      cartEntity.setRestaurantId(request.getRestaurantId());
      cartEntity.setStatus(LIVE_STATUS);
      cartEntity.setItems(new ArrayList<>());
      cartEntity.setTotal(0.0);
    } else {
      cartEntity = cartEntityOptional.get();
      
      if (!cartEntity.getRestaurantId().equals(request.getRestaurantId())) {
        throw new RuntimeException("Cannot add items from different restaurant");
      }
    }
    
    cartEntity.getItems().add(itemToAdd);
    double newTotal = cartEntity.getTotal() + itemToAdd.getPrice();
    cartEntity.setTotal(newTotal);
    
    CartEntity savedCart = cartRepository.save(cartEntity);
    Cart cart = convertToCart(savedCart);
    
    return new GetCartResponse(cart);
  }

  @Override
  public GetCartResponse removeItemFromCart(AddCartItemRequest request, String userId) {
    Optional<CartEntity> cartEntityOptional = cartRepository.findByIdAndUserId(request.getCartId(), userId);
    
    if (cartEntityOptional.isEmpty()) {
      throw new RuntimeException("Cart not found");
    }
    
    CartEntity cartEntity = cartEntityOptional.get();
    
    boolean removed = cartEntity.getItems().removeIf(item -> item.getItemId().equals(request.getItemId()));
    
    if (!removed) {
      throw new RuntimeException("Item not found in cart");
    }
    
    double newTotal = cartEntity.getItems().stream()
        .mapToDouble(ItemEntity::getPrice)
        .sum();
    cartEntity.setTotal(newTotal);
    
    CartEntity savedCart = cartRepository.save(cartEntity);
    Cart cart = convertToCart(savedCart);
    
    return new GetCartResponse(cart);
  }

  @Override
  public GetCartResponse clearCart(ClearCartRequest request, String userId) {
    Optional<CartEntity> cartEntityOptional = cartRepository.findByIdAndUserId(request.getCartId(), userId);
    
    if (cartEntityOptional.isEmpty()) {
      throw new RuntimeException("Cart not found");
    }
    
    CartEntity cartEntity = cartEntityOptional.get();
    cartEntity.setItems(new ArrayList<>());
    cartEntity.setTotal(0.0);
    
    CartEntity savedCart = cartRepository.save(cartEntity);
    Cart cart = convertToCart(savedCart);
    
    return new GetCartResponse(cart);
  }

  private Cart convertToCart(CartEntity cartEntity) {
    ModelMapper mapper = modelMapperProvider.get();
    List<Item> items = cartEntity.getItems().stream()
        .map(itemEntity -> mapper.map(itemEntity, Item.class))
        .collect(Collectors.toList());
    
    Cart cart = new Cart();
    cart.setId(cartEntity.getId());
    cart.setRestaurantId(cartEntity.getRestaurantId());
    cart.setUserId(cartEntity.getUserId());
    cart.setStatus(cartEntity.getStatus());
    cart.setItems(items);
    cart.setTotal(cartEntity.getTotal());
    
    return cart;
  }
}

