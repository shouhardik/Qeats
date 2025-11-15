package com.crio.qeats.exchanges;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class GetRestaurantsRequest {
    @NotNull
    @Max(value = 90)
    @Min(value = -90)
    private final Double latitude;
    @NotNull
    @Max(value = 180)
    @Min(value = -180)
    private final Double longitude;
    
    private String searchFor;


}

