package com.crio.qeats.exchanges;

import com.crio.qeats.dto.Order;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersResponse {
  
  private List<Order> orders;
  
}

