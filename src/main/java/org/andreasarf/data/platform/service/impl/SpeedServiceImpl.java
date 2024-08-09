package org.andreasarf.data.platform.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.speed.AverageSpeed;
import org.andreasarf.data.platform.repository.DataReader;
import org.andreasarf.data.platform.service.SpeedService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpeedServiceImpl implements SpeedService {

  @Qualifier("sparkDataReader")
  private final DataReader dataReader;

  @Override
  public Mono<AverageSpeed> getAvgSpeedIn24Hours(LocalDate date) {
    return Mono.fromCallable(() -> dataReader.getAvgSpeedIn24Hours(date))
        .subscribeOn(Schedulers.boundedElastic())
        .map(BigDecimal::new)
        .map(AverageSpeed::new);
  }
}
