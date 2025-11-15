package com.crio.qeats.repositories;

import com.crio.qeats.models.MenuEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<MenuEntity, String> {
  
  Optional<MenuEntity> findByRestaurantId(String restaurantId);
  
}

