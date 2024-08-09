package org.andreasarf.data.platform.dto.speed;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AverageSpeed implements Serializable {
  @JsonIgnore
  private BigDecimal averageSpeed;

  @JsonGetter("average_speed")
  public BigDecimal getRoundingAverageSpeed() {
    return averageSpeed.setScale(2, RoundingMode.FLOOR);
  }
}
