package org.andreasarf.data.platform.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.fare.AverageFareHeatmap;
import org.andreasarf.data.platform.model.FareHeatmap;
import org.andreasarf.data.platform.repository.DataReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.test.StepVerifier;

@SpringJUnitConfig(FareServiceImpl.class)
class FareServiceImplTest {

  @Autowired
  private FareServiceImpl service;
  @MockBean(name = "sparkDataReader")
  private DataReader dataReader;

  @Test
  void givenValidRequest_whenGetTotalTrips_thenShouldReturnData() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var response = List.of(
        new FareHeatmap(1.0, 1.0, 10.0),
        new FareHeatmap(1.0, 1.0001, 10.0),
        new FareHeatmap(140.0, 150.0, 20.0),
        new FareHeatmap(140.0001, 150.0, 20.0)
    );
    final var expected = List.of(
        new AverageFareHeatmap("1153277837910736896", new BigDecimal("10.0")),
        new AverageFareHeatmap("807284755295895552", new BigDecimal("20.0"))
    );

    when(dataReader.getFareHeatmap(date)).thenReturn(response);

    // act
    final var res = service.getAvgFareHeatmap(date);

    // arrange
    StepVerifier.create(res)
        .assertNext(actual -> assertThat(actual).hasSameElementsAs(expected))
        .verifyComplete();
  }

  @Test
  void givenReaderThrowError_whenGetTotalTrips_thenShouldThrow() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var expected = new RuntimeException("Error");

    when(dataReader.getFareHeatmap(date)).thenThrow(expected);

    // act
    final var res = service.getAvgFareHeatmap(date);

    // arrange
    StepVerifier.create(res)
        .expectError(expected.getClass())
        .verify();
  }
}
