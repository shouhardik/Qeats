package com.crio.qeats.exchanges;

import com.crio.qeats.dto.Restaurant;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class GetRestaurantsResponse {
    List<Restaurant> restaurants;

}

// }
