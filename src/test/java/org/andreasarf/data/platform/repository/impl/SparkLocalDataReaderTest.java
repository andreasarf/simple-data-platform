package org.andreasarf.data.platform.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.model.FareHeatmap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(SparkLocalDataReader.class)
class SparkLocalDataReaderTest {

  @Autowired
  private SparkLocalDataReader dataReader;
  @MockBean
  private SparkSession spark;

  @Mock
  private Dataset<Row> dataset;

  @Test
  void whenGetTotalTrips_thenShouldQuery() {
    // arrange
    final var start = LocalDate.parse("2024-08-08");
    final var end = LocalDate.parse("2024-08-31");
    final var totalTripDataset = mock(Dataset.class);
    final var totalTripList = List.of(
        new TotalTrip(LocalDate.parse("2024-08-08"), 10L),
        new TotalTrip(LocalDate.parse("2024-08-09"), 20L)
    );

    when(spark.sql(any())).thenReturn(dataset);
    when(dataset.as(Encoders.bean(TotalTrip.class))).thenReturn(totalTripDataset);
    when(totalTripDataset.collectAsList()).thenReturn(totalTripList);

    // act
    final var res = dataReader.getTotalTrips(start, end);

    // assert
    assertEquals(totalTripList, res);
  }

  @Test
  void givenDataExist_whenGetAvgSpeedIn24Hours_thenShouldQuery() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var row = mock(Row.class);
    final var avgSpeed = 10.90d;

    when(spark.sql(any())).thenReturn(dataset);
    when(dataset.first()).thenReturn(row);
    when(row.getDouble(0)).thenReturn(avgSpeed);

    // act
    final var res = dataReader.getAvgSpeedIn24Hours(date);

    // assert
    assertEquals(avgSpeed, res);
  }

  @Test
  void givenNullData_whenGetAvgSpeedIn24Hours_thenShouldQuery() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var avgSpeed = 0.0;

    when(spark.sql(any())).thenReturn(dataset);
    when(dataset.isEmpty()).thenReturn(true);

    // act
    final var res = dataReader.getAvgSpeedIn24Hours(date);

    // assert
    assertEquals(avgSpeed, res);
  }

  @Test
  void whenGetSpeedIn24Hours_thenShouldPrint() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");

    when(spark.sql(any())).thenReturn(dataset);

    // act
    dataReader.getSpeedIn24Hours(date);

    // assert
    verify(dataset).show();
  }

  @Test
  void whenGetFareHeatmap_thenShouldQuery() {
    // arrange
    final var date = LocalDate.parse("2024-08-08");
    final var fareHeatmapDataset = mock(Dataset.class);
    final var fareHeatmapList = List.of(
        new FareHeatmap(10.0, 20.0, 30.0),
        new FareHeatmap(40.0, 50.0, 60.0)
    );

    when(spark.sql(any())).thenReturn(dataset);
    when(dataset.as(Encoders.bean(FareHeatmap.class))).thenReturn(fareHeatmapDataset);
    when(fareHeatmapDataset.collectAsList()).thenReturn(fareHeatmapList);

    // act
    final var res = dataReader.getFareHeatmap(date);

    // assert
    assertEquals(fareHeatmapList, res);
  }
}
