package com.crio.qeats.repositories;

import com.crio.qeats.models.OrderEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
  
  List<OrderEntity> findByUserId(String userId);
  
}

