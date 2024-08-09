package org.andreasarf.data.platform.service;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import reactor.core.publisher.Mono;

public interface TripService {

  Mono<List<TotalTrip>> getTotalTrips(LocalDate startDate, LocalDate endDate);
}
