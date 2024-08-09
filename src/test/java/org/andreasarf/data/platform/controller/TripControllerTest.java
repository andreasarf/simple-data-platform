package org.andreasarf.data.platform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.common.ApiConstant;
import org.andreasarf.data.platform.common.ErrorReason;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.service.TripService;
import org.andreasarf.data.platform.util.JacksonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(TripController.class)
@TestPropertySource(properties = "logging.level.root=off")
class TripControllerTest {

  @Autowired
  private WebTestClient client;
  @MockBean
  private TripService tripService;

  @Test
  void givenValidRequest_thenGetTotalTrips_thenShouldRespond200() {
    // arrange
    final var startDate = LocalDate.parse("2024-08-08");
    final var endDate = LocalDate.parse("2024-08-31");
    final var expectedResponse = List.of(
        new TotalTrip(LocalDate.parse("2024-08-08"), 10L),
        new TotalTrip(LocalDate.parse("2024-08-09"), 20L));
    final var expectedBody = BaseResponse.ok(expectedResponse);

    when(tripService.getTotalTrips(startDate, endDate))
        .thenReturn(Mono.just(expectedResponse));

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_TOTAL_TRIPS)
            .queryParam("start", startDate)
            .queryParam("end", endDate)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(tripService).getTotalTrips(startDate, endDate);
  }

  @Test
  void givenInvalidEndDate_whenGetTotalTrips_thenShouldRespond400() {
    // arrange
    final var startDate = LocalDate.parse("2024-08-08");
    final var endDate = LocalDate.parse("2024-08-07");
    final var expectedBody = BaseResponse.invalidParam(ErrorReason.END_START_DATE);

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_TOTAL_TRIPS)
            .queryParam("start", startDate)
            .queryParam("end", endDate)
            .build())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(tripService, never()).getTotalTrips(any(), any());
  }

  @Test
  void givenMissingEndDate_whenGetTotalTrips_thenShouldRespond400() {
    // arrange
    final var startDate = LocalDate.parse("2024-08-08");
    final var expectedBody = BaseResponse.invalidParam(
        "Required query parameter 'end' is not present.");

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_TOTAL_TRIPS)
            .queryParam("start", startDate)
            .build())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(tripService, never()).getTotalTrips(any(), any());
  }

  @Test
  void givenServiceThrowError_whenGetTotalTrips_thenShouldRespond500() {
    // arrange
    final var startDate = LocalDate.parse("2024-08-08");
    final var endDate = LocalDate.parse("2024-08-09");
    final var expectedBody = BaseResponse.internalError(
        "An unexpected error occurred: Internal Server Error");

    when(tripService.getTotalTrips(startDate, endDate))
        .thenThrow(new RuntimeException("Internal Server Error"));

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_TOTAL_TRIPS)
            .queryParam("start", startDate)
            .queryParam("end", endDate)
            .build())
        .exchange()
        .expectStatus().is5xxServerError()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(tripService).getTotalTrips(startDate, endDate);
  }
}
