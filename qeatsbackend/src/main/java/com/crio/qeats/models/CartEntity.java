package com.crio.qeats.models;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "carts")
@NoArgsConstructor
public class CartEntity {

  @Id
  private String id;

  @NotNull
  private String restaurantId;

  @NotNull
  private String userId;

  @NotNull
  private String status;

  @NotNull
  private List<ItemEntity> items = new ArrayList<>();

  @NotNull
  private Double total;

}

