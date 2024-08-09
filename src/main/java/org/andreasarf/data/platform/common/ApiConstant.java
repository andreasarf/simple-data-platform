package org.andreasarf.data.platform.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiConstant {

  public static final String V1_ROOT = "/api/v1/data";
  public static final String V1_ROOT_INTERNAL = "/api/v1/internal/data";

  // trip
  public static final String GET_TOTAL_TRIPS = "/total-trips";

  // fare
  public static final String GET_AVG_FARE_HEATMAP = "/average-fare-heatmap";

  // speed
  public static final String GET_AVG_SPEED_24H = "/average-speed-24hrs";
}
