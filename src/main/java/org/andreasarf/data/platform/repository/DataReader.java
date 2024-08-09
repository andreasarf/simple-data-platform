package org.andreasarf.data.platform.repository;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.model.FareHeatmap;

public interface DataReader {

  List<TotalTrip> getTotalTrips(LocalDate start, LocalDate end);

  Double getAvgSpeedIn24Hours(LocalDate date);

  void getSpeedIn24Hours(LocalDate date);

  List<FareHeatmap> getFareHeatmap(LocalDate date);
}
