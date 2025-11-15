package com.crio.qeats.controller;

import com.crio.qeats.exchanges.AddCartItemRequest;
import com.crio.qeats.exchanges.ClearCartRequest;
import com.crio.qeats.exchanges.GetCartResponse;
import com.crio.qeats.exchanges.GetMenuResponse;
import com.crio.qeats.exchanges.GetOrdersResponse;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.exchanges.PostOrderRequest;
import com.crio.qeats.exchanges.PostOrderResponse;
import com.crio.qeats.services.CartService;
import com.crio.qeats.services.MenuService;
import com.crio.qeats.services.OrderService;
import com.crio.qeats.services.RestaurantService;
import java.time.LocalTime;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(RestaurantController.RESTAURANT_API_ENDPOINT)
public class RestaurantController {

  public static final String RESTAURANT_API_ENDPOINT = "/qeats/v1";
  public static final String RESTAURANTS_API = "/restaurants";
  public static final String MENU_API = "/menu";
  public static final String CART_API = "/cart";
  public static final String CART_ITEM_API = "/cart/item";
  public static final String CART_CLEAR_API = "/cart/clear";
  public static final String POST_ORDER_API = "/order";
  public static final String GET_ORDERS_API = "/orders";

  @Autowired
  private RestaurantService restaurantService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private CartService cartService;

  @Autowired
  private OrderService orderService;



  @GetMapping(RESTAURANTS_API)
  public ResponseEntity<GetRestaurantsResponse> getRestaurants(
       GetRestaurantsRequest getRestaurantsRequest) {

      Double latitude = getRestaurantsRequest.getLatitude();
      Double longitude=  getRestaurantsRequest.getLongitude();
      if(latitude==(null)||longitude==(null)) {
        return ResponseEntity.badRequest().body(null);
      }
      if(latitude>=90||latitude<=-90||longitude>=180||longitude<=-180){
        return ResponseEntity.badRequest().body(null);
      }
      
      

    log.info("getRestaurants called with {}", getRestaurantsRequest);
    GetRestaurantsResponse getRestaurantsResponse;

      //CHECKSTYLE:OFF
      getRestaurantsResponse = restaurantService
          .findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());
      log.info("getRestaurants returned {}", getRestaurantsResponse);
      //CHECKSTYLE:ON
      //if(RESTAURANTS_API==RESTAURANT_API_ENDPOINT)
    // log.info("getRestaurants called with {}", getRestaurantsRequest);
    // GetRestaurantsResponse getRestaurantsResponse;

    //   getRestaurantsResponse = restaurantService
    //       .findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());
    //   log.info("getRestaurants returned {}", getRestaurantsResponse);

    return ResponseEntity.ok().body(getRestaurantsResponse);
  }

  @GetMapping(MENU_API)
  public ResponseEntity<GetMenuResponse> getMenu(
      @RequestParam(value = "restaurantId", required = true) String restaurantId) {
    
    if (restaurantId == null || restaurantId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("getMenu called with restaurantId: {}", restaurantId);
    GetMenuResponse getMenuResponse = menuService.findMenuByRestaurantId(restaurantId);
    log.info("getMenu returned {}", getMenuResponse);
    
    return ResponseEntity.ok().body(getMenuResponse);
  }










  @GetMapping(CART_API)
  public ResponseEntity<GetCartResponse> getCart(
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("getCart called with userId: {}", userId);
    GetCartResponse getCartResponse = cartService.findCartByUserId(userId);
    log.info("getCart returned {}", getCartResponse);
    
    return ResponseEntity.ok().body(getCartResponse);
  }

  @PostMapping(CART_ITEM_API)
  public ResponseEntity<GetCartResponse> addItemToCart(
      @RequestBody AddCartItemRequest addCartItemRequest,
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("addItemToCart called with request: {} and userId: {}", addCartItemRequest, userId);
    try {
      GetCartResponse getCartResponse = cartService.addItemToCart(addCartItemRequest, userId);
      log.info("addItemToCart returned {}", getCartResponse);
      return ResponseEntity.ok().body(getCartResponse);
    } catch (Exception e) {
      log.error("Error adding item to cart", e);
      return ResponseEntity.badRequest().body(null);
    }
  }

  @DeleteMapping(CART_ITEM_API)
  public ResponseEntity<GetCartResponse> removeItemFromCart(
      @RequestBody AddCartItemRequest removeCartItemRequest,
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("removeItemFromCart called with request: {} and userId: {}", removeCartItemRequest, userId);
    try {
      GetCartResponse getCartResponse = cartService.removeItemFromCart(removeCartItemRequest, userId);
      log.info("removeItemFromCart returned {}", getCartResponse);
      return ResponseEntity.ok().body(getCartResponse);
    } catch (Exception e) {
      log.error("Error removing item from cart", e);
      return ResponseEntity.badRequest().body(null);
    }
  }

  @PostMapping(CART_CLEAR_API)
  public ResponseEntity<GetCartResponse> clearCart(
      @RequestBody ClearCartRequest clearCartRequest,
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("clearCart called with request: {} and userId: {}", clearCartRequest, userId);
    try {
      GetCartResponse getCartResponse = cartService.clearCart(clearCartRequest, userId);
      log.info("clearCart returned {}", getCartResponse);
      return ResponseEntity.ok().body(getCartResponse);
    } catch (Exception e) {
      log.error("Error clearing cart", e);
      return ResponseEntity.badRequest().body(null);
    }
  }
}

  @PostMapping(POST_ORDER_API)
  public ResponseEntity<PostOrderResponse> placeOrder(
      @RequestBody PostOrderRequest postOrderRequest,
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("placeOrder called with request: {} and userId: {}", postOrderRequest, userId);
    try {
      PostOrderResponse postOrderResponse = orderService.placeOrder(postOrderRequest, userId);
      log.info("placeOrder returned {}", postOrderResponse);
      return ResponseEntity.ok().body(postOrderResponse);
    } catch (Exception e) {
      log.error("Error placing order", e);
      return ResponseEntity.badRequest().body(null);
    }
  }

  @GetMapping(GET_ORDERS_API)
  public ResponseEntity<GetOrdersResponse> getOrders(
      @RequestParam(value = "userId", required = true) String userId) {
    
    if (userId == null || userId.trim().isEmpty()) {
      return ResponseEntity.badRequest().body(null);
    }
    
    log.info("getOrders called with userId: {}", userId);
    GetOrdersResponse getOrdersResponse = orderService.getOrdersByUserId(userId);
    log.info("getOrders returned {}", getOrdersResponse);
    
    return ResponseEntity.ok().body(getOrdersResponse);
  }
