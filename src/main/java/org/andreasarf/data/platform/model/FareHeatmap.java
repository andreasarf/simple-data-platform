package org.andreasarf.data.platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FareHeatmap {

  private Double pickupLatitude;
  private Double pickupLongitude;
  private Double fare;
}
