package org.andreasarf.data.platform.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.repository.DataReader;
import org.andreasarf.data.platform.service.TripService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

  @Qualifier("sparkDataReader")
  private final DataReader dataReader;

  @Override
  public Mono<List<TotalTrip>> getTotalTrips(LocalDate startDate, LocalDate endDate) {
    return Mono.fromCallable(() -> dataReader.getTotalTrips(startDate, endDate))
        .subscribeOn(Schedulers.boundedElastic());
  }
}
