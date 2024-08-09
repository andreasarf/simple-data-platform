package org.andreasarf.data.platform.service.impl;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.andreasarf.data.platform.dto.speed.AverageSpeed;
import org.andreasarf.data.platform.repository.DataReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.test.StepVerifier;

@SpringJUnitConfig(SpeedServiceImpl.class)
class SpeedServiceImplTest {

  @Autowired
  private SpeedServiceImpl service;
  @MockBean(name = "sparkDataReader")
  private DataReader dataReader;

  @Test
  void givenValidRequest_whenGetTotalTrips_thenShouldReturnData() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var response = 40.98d;
    final var expected = new AverageSpeed(new BigDecimal(response));

    when(dataReader.getAvgSpeedIn24Hours(date)).thenReturn(response);

    // act
    final var res = service.getAvgSpeedIn24Hours(date);

    // arrange
    StepVerifier.create(res)
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  void givenReaderThrowError_whenGetTotalTrips_thenShouldThrow() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var expected = new RuntimeException("Error");

    when(dataReader.getAvgSpeedIn24Hours(date)).thenThrow(expected);

    // act
    final var res = service.getAvgSpeedIn24Hours(date);

    // arrange
    StepVerifier.create(res)
        .expectError(expected.getClass())
        .verify();
  }
}
