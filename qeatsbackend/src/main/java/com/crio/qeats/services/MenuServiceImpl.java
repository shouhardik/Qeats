package com.crio.qeats.services;

import com.crio.qeats.dto.Item;
import com.crio.qeats.dto.Menu;
import com.crio.qeats.exchanges.GetMenuResponse;
import com.crio.qeats.models.ItemEntity;
import com.crio.qeats.models.MenuEntity;
import com.crio.qeats.repositories.MenuRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Provider;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MenuServiceImpl implements MenuService {

  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  @Override
  public GetMenuResponse findMenuByRestaurantId(String restaurantId) {
    Optional<MenuEntity> menuEntityOptional = menuRepository.findByRestaurantId(restaurantId);
    
    if (menuEntityOptional.isEmpty()) {
      return new GetMenuResponse(new Menu(restaurantId, new ArrayList<>()));
    }
    
    MenuEntity menuEntity = menuEntityOptional.get();
    ModelMapper mapper = modelMapperProvider.get();
    
    List<Item> items = menuEntity.getItems().stream()
        .map(itemEntity -> mapper.map(itemEntity, Item.class))
        .collect(Collectors.toList());
    
    Menu menu = new Menu(menuEntity.getRestaurantId(), items);
    return new GetMenuResponse(menu);
  }
}

