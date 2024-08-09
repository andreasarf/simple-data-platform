package org.andreasarf.data.platform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.common.ApiConstant;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.dto.fare.AverageFareHeatmap;
import org.andreasarf.data.platform.service.FareService;
import org.andreasarf.data.platform.util.JacksonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(FareController.class)
@TestPropertySource(properties = "logging.level.root=off")
class FareControllerTest {

  @Autowired
  private WebTestClient client;
  @MockBean
  private FareService fareService;

  @Test
  void givenValidRequest_thenGetAvgFareHeatmap_thenShouldRespond200() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var expectedResponse = List.of(
        new AverageFareHeatmap("12345", BigDecimal.valueOf(10.0)),
        new AverageFareHeatmap("56712", BigDecimal.valueOf(20.0))
    );
    final var expectedBody = BaseResponse.ok(expectedResponse);

    when(fareService.getAvgFareHeatmap(date))
        .thenReturn(Mono.just(expectedResponse));

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_AVG_FARE_HEATMAP)
            .queryParam("date", date)
            .build())
        .exchange()
        .expectStatus().isOk()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(fareService).getAvgFareHeatmap(date);
  }

  @Test
  void givenInvalidDate_whenGetAvgFareHeatmap_thenShouldRespond400() {
    // arrange
    final var date = "2024-08-32";
    final var expectedBody = BaseResponse.invalidParam("Type mismatch.");

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_AVG_FARE_HEATMAP)
            .queryParam("date", date)
            .build())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(fareService, never()).getAvgFareHeatmap(any());
  }

  @Test
  void givenMissingEndDate_whenGetAvgFareHeatmap_thenShouldRespond400() {
    // arrange
    final var expectedBody = BaseResponse.invalidParam(
        "Required query parameter 'date' is not present.");

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_AVG_FARE_HEATMAP)
            .build())
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(fareService, never()).getAvgFareHeatmap(any());
  }

  @Test
  void givenServiceThrowError_whenGetAvgFareHeatmap_thenShouldRespond500() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var expectedBody = BaseResponse.internalError(
        "An unexpected error occurred: Internal Server Error");

    when(fareService.getAvgFareHeatmap(date))
        .thenThrow(new RuntimeException("Internal Server Error"));

    // act
    client.get()
        .uri(uriBuilder -> uriBuilder.path(ApiConstant.V1_ROOT + ApiConstant.GET_AVG_FARE_HEATMAP)
            .queryParam("date", date)
            .build())
        .exchange()
        .expectStatus().is5xxServerError()
        .expectBody().json(JacksonMapper.writeValueAsString(expectedBody), true);

    // assert
    verify(fareService).getAvgFareHeatmap(date);
  }
}
