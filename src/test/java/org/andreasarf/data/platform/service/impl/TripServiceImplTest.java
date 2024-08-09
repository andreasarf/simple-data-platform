package org.andreasarf.data.platform.service.impl;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.repository.DataReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.test.StepVerifier;

@SpringJUnitConfig(TripServiceImpl.class)
class TripServiceImplTest {

  @Autowired
  private TripServiceImpl service;
  @MockBean(name = "sparkDataReader")
  private DataReader dataReader;

  @Test
  void givenValidRequest_whenGetTotalTrips_thenShouldReturnData() {
    // arrange
    final var start = LocalDate.parse("2024-08-08");
    final var end = LocalDate.parse("2024-08-31");
    final var expected = List.of(
        new TotalTrip(LocalDate.parse("2024-08-08"), 10L),
        new TotalTrip(LocalDate.parse("2024-08-09"), 20L));

    when(dataReader.getTotalTrips(start, end)).thenReturn(expected);

    // act
    final var res = service.getTotalTrips(start, end);

    // arrange
    StepVerifier.create(res)
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  void givenReaderThrowError_whenGetTotalTrips_thenShouldThrow() {
    // arrange
    final var start = LocalDate.parse("2024-08-08");
    final var end = LocalDate.parse("2024-08-31");
    final var expected = new RuntimeException("Error");

    when(dataReader.getTotalTrips(start, end)).thenThrow(expected);

    // act
    final var res = service.getTotalTrips(start, end);

    // arrange
    StepVerifier.create(res)
        .expectError(expected.getClass())
        .verify();
  }
}
