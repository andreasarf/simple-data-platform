package org.andreasarf.data.platform.repository.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.model.FareHeatmap;
import org.andreasarf.data.platform.repository.DataReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.schema.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HadoopLocalDataReader implements DataReader {

  @Value("${dataset.file.path}")
  private String datasetPath;

  private static List<SimpleGroup> extractSimpleGroups(ParquetFileReader reader, MessageType schema)
      throws IOException {
    final List<SimpleGroup> simpleGroups = new ArrayList<>();
    PageReadStore pages;
    while ((pages = reader.readNextRowGroup()) != null) {
      final var rows = pages.getRowCount();
      final var columnId = new ColumnIOFactory().getColumnIO(schema);
      final var recordReader = columnId.getRecordReader(pages, new GroupRecordConverter(schema));

      for (int i = 0; i < rows; i++) {
        final var simpleGroup = (SimpleGroup) recordReader.read();
        simpleGroups.add(simpleGroup);
      }
    }
    return simpleGroups;
  }

  @SneakyThrows
  public void readData() {
    log.info("Reading data from Hadoop: {}", datasetPath);
    try (final var reader = ParquetFileReader.open(HadoopInputFile
        .fromPath(new Path(datasetPath), new Configuration()))) {
      final var schema = reader.getFooter().getFileMetaData().getSchema();
      final var fields = schema.getFields();
      final var simpleGroups = extractSimpleGroups(reader, schema);
      log.info("Schema: {}", schema);
      log.info("Fields: {}", fields);
      log.info("Simple groups: {}", simpleGroups);
    } catch (Exception e) {
      log.error("Error reading data from Hadoop: {}", e.getMessage());
    }
  }

  @Override
  public List<TotalTrip> getTotalTrips(LocalDate start, LocalDate end) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Double getAvgSpeedIn24Hours(LocalDate date) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void getSpeedIn24Hours(LocalDate date) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public List<FareHeatmap> getFareHeatmap(LocalDate date) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
