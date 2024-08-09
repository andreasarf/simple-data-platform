package org.andreasarf.data.platform.service;

import java.time.LocalDate;
import org.andreasarf.data.platform.dto.speed.AverageSpeed;
import reactor.core.publisher.Mono;

public interface SpeedService {

  Mono<AverageSpeed> getAvgSpeedIn24Hours(LocalDate date);
}
