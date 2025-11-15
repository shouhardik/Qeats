package com.crio.qeats.exchanges;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCartItemRequest {
  
  @NotNull
  private String cartId;
  
  @NotNull
  private String itemId;
  
  @NotNull
  private String restaurantId;
  
}

