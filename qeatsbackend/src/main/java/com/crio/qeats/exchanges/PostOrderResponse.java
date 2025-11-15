package com.crio.qeats.exchanges;

import com.crio.qeats.dto.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderResponse {
  
  private Order order;
  
}

