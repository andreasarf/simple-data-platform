package org.andreasarf.data.platform.repository.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.model.FareHeatmap;
import org.andreasarf.data.platform.repository.DataReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service("sparkDataReader")
@RequiredArgsConstructor
public class SparkLocalDataReader implements DataReader {

  private static final String MSG_EXECUTING_SQL = "Executing SQL: {}";
  private static final String SQL_TOTAL_TRIP = """
      SELECT DATE(trip_start_timestamp) `date`, count(*) totalTrips
      FROM trip
      GROUP BY DATE(trip_start_timestamp)
      HAVING DATE(trip_start_timestamp) BETWEEN '%s' AND '%s'
      ORDER BY DATE(trip_start_timestamp)""";
  private static final String SQL_AVG_SPEED_24H = """
      SELECT avg(speed) averageSpeed
      FROM (
        SELECT trip_end_timestamp, (trip_miles*1.60934)/(trip_seconds/60.0/60.0) speed
        FROM trip
        WHERE DATE(trip_end_timestamp) = '%s' AND trip_miles > 0 AND trip_seconds > 0
      )""";
  private static final String SQL_SPEED_24H = """
      SELECT trip_end_timestamp, trip_miles, trip_miles*1.60934 trip_km, trip_seconds,
        trip_seconds/60.0/60.0 trip_hours, (trip_miles*1.60934)/(trip_seconds/60.0/60.0) speed
      FROM trip
      WHERE DATE(trip_end_timestamp) = '%s'""";
  private static final String SQL_FARE_HEATMAP = """
      SELECT pickup_latitude pickupLatitude, pickup_longitude pickupLongitude, fare
      FROM trip
      WHERE DATE(trip_start_timestamp) = '%s' AND fare > 0 
        AND pickup_latitude IS NOT NULL AND pickup_longitude IS NOT NULL""";

  private final SparkSession spark;

  @Override
  public List<TotalTrip> getTotalTrips(LocalDate start, LocalDate end) {
    final var sql = String.format(SQL_TOTAL_TRIP, start, end);
    final var data = execSql(sql);
    return data.as(Encoders.bean(TotalTrip.class))
        .collectAsList();
  }

  @Override
  public Double getAvgSpeedIn24Hours(LocalDate date) {
    final var sql = String.format(SQL_AVG_SPEED_24H, date);
    final var data = execSql(sql);
    //getSpeedIn24Hours(date);
    return data.isEmpty() ? 0.0 : data.first().getDouble(0);
  }

  @Override
  public void getSpeedIn24Hours(LocalDate date) {
    final var sql = String.format(SQL_SPEED_24H, date);
    final var data = execSql(sql);
    data.show();
  }

  @Override
  public List<FareHeatmap> getFareHeatmap(LocalDate date) {
    final var sql = String.format(SQL_FARE_HEATMAP, date);
    final var data = execSql(sql);
    return data.as(Encoders.bean(FareHeatmap.class))
        .collectAsList();
  }

  private Dataset<Row> execSql(String sql) {
    log.debug(MSG_EXECUTING_SQL, sql);
    return spark.sql(sql);
  }

}
