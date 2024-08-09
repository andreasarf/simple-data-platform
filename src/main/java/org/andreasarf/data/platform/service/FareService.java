package org.andreasarf.data.platform.service;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.fare.AverageFareHeatmap;
import reactor.core.publisher.Mono;

public interface FareService {

  Mono<List<AverageFareHeatmap>> getAvgFareHeatmap(LocalDate date);
}
