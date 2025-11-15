package com.crio.qeats.services;

import com.crio.qeats.exchanges.GetMenuResponse;

public interface MenuService {
  
  GetMenuResponse findMenuByRestaurantId(String restaurantId);
  
}

