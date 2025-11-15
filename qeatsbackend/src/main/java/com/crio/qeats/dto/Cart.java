package com.crio.qeats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {
  
  @NotNull
  private String id;
  
  @NotNull
  private String restaurantId;
  
  @NotNull
  private String userId;
  
  private String status;
  
  @NotNull
  private List<Item> items;
  
  @NotNull
  private Double total;
  
}

