package com.crio.qeats.services;

import com.crio.qeats.exchanges.GetOrdersResponse;
import com.crio.qeats.exchanges.PostOrderRequest;
import com.crio.qeats.exchanges.PostOrderResponse;

public interface OrderService {
  
  PostOrderResponse placeOrder(PostOrderRequest postOrderRequest, String userId);
  
  GetOrdersResponse getOrdersByUserId(String userId);
  
}

