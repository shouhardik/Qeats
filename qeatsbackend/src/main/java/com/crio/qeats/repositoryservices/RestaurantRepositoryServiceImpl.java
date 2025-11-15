package com.crio.qeats.repositoryservices;

import ch.hsr.geohash.GeoHash;
import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.globals.GlobalConstants;
import com.crio.qeats.models.RestaurantEntity;
import com.crio.qeats.repositories.RestaurantRepository;
import com.crio.qeats.utils.GeoLocation;
import com.crio.qeats.utils.GeoUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.inject.Provider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;


@Service
public class RestaurantRepositoryServiceImpl implements RestaurantRepositoryService {




  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  
  @Autowired
  private RestaurantRepository dataSet;

  private boolean isOpenNow(LocalTime time, RestaurantEntity res) {
    LocalTime openingTime = LocalTime.parse(res.getOpensAt());
    LocalTime closingTime = LocalTime.parse(res.getClosesAt());

    return time.isAfter(openingTime) && time.isBefore(closingTime);
  }

  public List<Restaurant> findAllRestaurantsCloseBy(Double latitude,
      Double longitude, LocalTime currentTime, Double servingRadiusInKms) {

    List<Restaurant> restaurants = new ArrayList<>();
    List<RestaurantEntity> tmp = new ArrayList<RestaurantEntity>();
    List<Restaurant> myList=new ArrayList<Restaurant>();

    try{
      tmp=dataSet.findAll();
    }catch(NullPointerException e){
      e.printStackTrace();
    }

    ModelMapper mapperClass=modelMapperProvider.get();
    for(RestaurantEntity et:tmp){
      restaurants.add(mapperClass.map(et,Restaurant.class));
    }
    for(Restaurant res:restaurants){
      double restaurantLat=res.getLatitude();
      double restaurantLon=res.getLongitude();
      LocalTime opensAt=LocalTime.parse(res.getOpensAt());
      LocalTime closeAt=LocalTime.parse(res.getClosesAt());
      double distanceKm = GeoUtils.findDistanceInKm(
        latitude, longitude, restaurantLat, restaurantLon);
        if (Double.compare(distanceKm, servingRadiusInKms) > 0) {
          continue;
        }
        boolean result=isValidTime(currentTime,opensAt,closeAt);
        if(result==false){
          continue;
        }
        myList.add(res);
    }
    return myList;
  }





    
    private boolean isValidTime(LocalTime current, 
      LocalTime open, LocalTime close) {
    boolean result = false;
      if (current.equals(open) || current.equals(close)) {
        result = true;
      }
      if (current.isAfter(open) && current.isBefore(close)) {
        result = true;
      }
    return result;
    }
    










  /**
   * Utility method to check if a restaurant is within the serving radius at a given time.
   * @return boolean True if restaurant falls within serving radius and is open, false otherwise
   */
  private boolean isRestaurantCloseByAndOpen(RestaurantEntity restaurantEntity,
      LocalTime currentTime, Double latitude, Double longitude, Double servingRadiusInKms) {
    if (isOpenNow(currentTime, restaurantEntity)) {
      return GeoUtils.findDistanceInKm(latitude, longitude,
          restaurantEntity.getLatitude(), restaurantEntity.getLongitude())
          < servingRadiusInKms;
    }

    return false;
  }



}

