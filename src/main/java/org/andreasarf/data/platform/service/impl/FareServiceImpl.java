package org.andreasarf.data.platform.service.impl;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.fare.AverageFareHeatmap;
import org.andreasarf.data.platform.model.FareHeatmap;
import org.andreasarf.data.platform.repository.DataReader;
import org.andreasarf.data.platform.service.FareService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class FareServiceImpl implements FareService {

  private static final int STD_S2_LEVEL = 16;

  @Qualifier("sparkDataReader")
  private final DataReader dataReader;

  private static Mono<List<AverageFareHeatmap>> getAvgFareHeatmapInternal(
      List<FareHeatmap> fareHeatmaps) {
    final var heatmap = computeHeatmap(fareHeatmaps);
    return Mono.just(computeAverageHeatmap(heatmap));
  }

  private static Map<Long, List<Double>> computeHeatmap(List<FareHeatmap> fareHeatmaps) {
    final Map<Long, List<Double>> heatmap = Collections.synchronizedMap(new HashMap<>());
    fareHeatmaps.parallelStream()
        .forEach(fareHeatmap -> {
          final var latLong = S2LatLng.fromDegrees(fareHeatmap.getPickupLatitude(),
              fareHeatmap.getPickupLongitude());
          final var cellId = S2CellId.fromLatLng(latLong)
              .parent(STD_S2_LEVEL)
              .id();
          heatmap.computeIfAbsent(cellId, k -> Collections.synchronizedList(new ArrayList<>()))
              .add(fareHeatmap.getFare());
        });
    return heatmap;
  }

  private static List<AverageFareHeatmap> computeAverageHeatmap(Map<Long, List<Double>> heatmap) {
    final List<AverageFareHeatmap> avgHeatmap = Collections.synchronizedList(new ArrayList<>());
    heatmap.entrySet().parallelStream()
        .forEach(entry -> {
          final var fareList = entry.getValue();
          final var avg = fareList.stream()
              .mapToDouble(Double::doubleValue)
              .average();
          avgHeatmap.add(new AverageFareHeatmap(entry.getKey().toString(),
              BigDecimal.valueOf(avg.orElse(0.0))));
        });
    return avgHeatmap;
  }

  @Override
  public Mono<List<AverageFareHeatmap>> getAvgFareHeatmap(LocalDate date) {
    return Mono.fromCallable(() -> dataReader.getFareHeatmap(date))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(FareServiceImpl::getAvgFareHeatmapInternal);
  }
}
