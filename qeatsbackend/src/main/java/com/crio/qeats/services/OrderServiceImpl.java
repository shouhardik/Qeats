package com.crio.qeats.services;

import com.crio.qeats.dto.Item;
import com.crio.qeats.dto.Order;
import com.crio.qeats.exchanges.GetOrdersResponse;
import com.crio.qeats.exchanges.PostOrderRequest;
import com.crio.qeats.exchanges.PostOrderResponse;
import com.crio.qeats.models.CartEntity;
import com.crio.qeats.models.ItemEntity;
import com.crio.qeats.models.OrderEntity;
import com.crio.qeats.repositories.CartRepository;
import com.crio.qeats.repositories.OrderRepository;
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
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  private static final String LIVE_STATUS = "LIVE";

  @Override
  public PostOrderResponse placeOrder(PostOrderRequest request, String userId) {
    Optional<CartEntity> cartEntityOptional = cartRepository.findByIdAndUserId(request.getCartId(), userId);
    
    if (cartEntityOptional.isEmpty()) {
      throw new RuntimeException("Cart not found");
    }
    
    CartEntity cartEntity = cartEntityOptional.get();
    
    if (cartEntity.getItems().isEmpty()) {
      throw new RuntimeException("Cannot place order with empty cart");
    }
    
    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setUserId(userId);
    orderEntity.setRestaurantId(cartEntity.getRestaurantId());
    orderEntity.setItems(new ArrayList<>(cartEntity.getItems()));
    orderEntity.setTotal(cartEntity.getTotal());
    
    OrderEntity savedOrder = orderRepository.save(orderEntity);
    
    cartEntity.setItems(new ArrayList<>());
    cartEntity.setTotal(0.0);
    cartRepository.save(cartEntity);
    
    Order order = convertToOrder(savedOrder);
    return new PostOrderResponse(order);
  }

  @Override
  public GetOrdersResponse getOrdersByUserId(String userId) {
    List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);
    
    List<Order> orders = orderEntities.stream()
        .map(this::convertToOrder)
        .collect(Collectors.toList());
    
    return new GetOrdersResponse(orders);
  }

  private Order convertToOrder(OrderEntity orderEntity) {
    ModelMapper mapper = modelMapperProvider.get();
    List<Item> items = orderEntity.getItems().stream()
        .map(itemEntity -> mapper.map(itemEntity, Item.class))
        .collect(Collectors.toList());
    
    Order order = new Order();
    order.setId(orderEntity.getId());
    order.setRestaurantId(orderEntity.getRestaurantId());
    order.setUserId(orderEntity.getUserId());
    order.setItems(items);
    order.setTotal(orderEntity.getTotal());
    
    return order;
  }
}

