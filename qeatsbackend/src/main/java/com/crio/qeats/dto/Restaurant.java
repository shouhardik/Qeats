
package com.crio.qeats.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {
   @NotNull
   private String restaurantId;
   @NotNull
   private String name;
   @NotNull
   private String city;
   @NotNull
   private String imageUrl;
   @NotNull
   private Double latitude;
   @NotNull
   private Double longitude;
   @NotNull
   private String opensAt;
   @NotNull
   private String closesAt;
   @NotNull
   private String[] attributes;
  
}


