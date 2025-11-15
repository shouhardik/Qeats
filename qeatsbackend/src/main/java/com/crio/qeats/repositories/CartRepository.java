package com.crio.qeats.repositories;

import com.crio.qeats.models.CartEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<CartEntity, String> {
  
  Optional<CartEntity> findByIdAndUserId(String id, String userId);
  
  Optional<CartEntity> findByUserIdAndStatus(String userId, String status);
  
}

