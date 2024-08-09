package org.andreasarf.data.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SparkConfig {

  @Value("${dataset.file.path}")
  private String datasetPath;

  @Bean
  public SparkSession spark() {
    log.info("Booting up Spark");
    final var spark = SparkSession.builder()
        .appName("Data Platform")
        .master("local")
        .getOrCreate();

    log.info("Reading data from: {}", datasetPath);
    final var dataframe = spark.read().parquet(datasetPath);
    dataframe.printSchema();
    dataframe.createOrReplaceTempView("trip");

    return spark;
  }
}
