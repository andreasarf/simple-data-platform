package org.andreasarf.data.platform.dto.fare;

import java.math.BigDecimal;
import org.andreasarf.data.platform.util.JacksonMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

class AverageFareHeatmapTest {

  @Test
  void givenObject_whenSerialize_thenShouldReturnJson() throws Exception {
    // arrange
    AverageFareHeatmap averageFareHeatmap = new AverageFareHeatmap()
        .setS2id("s2id")
        .setFare(BigDecimal.valueOf(23.456));
    String expected = """
        {
          "s2id": "s2id",
          "fare": 23.45
        }""";

    // act
    final var actual = JacksonMapper.writeValueAsString(averageFareHeatmap);

    // assert
    JSONAssert.assertEquals(expected, actual, true);
  }
}
