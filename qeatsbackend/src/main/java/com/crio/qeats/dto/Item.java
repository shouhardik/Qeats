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
public class Item {
  
  @NotNull
  private String id;
  
  @NotNull
  private String itemId;
  
  @NotNull
  private String name;
  
  @NotNull
  private String imageUrl;
  
  @NotNull
  private Double price;
  
  @NotNull
  private List<String> attributes;
  
}

