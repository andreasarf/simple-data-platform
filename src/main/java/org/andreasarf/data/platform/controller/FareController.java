package org.andreasarf.data.platform.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.common.ApiConstant;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.dto.fare.AverageFareHeatmap;
import org.andreasarf.data.platform.service.FareService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.V1_ROOT)
public class FareController {

  private final FareService fareService;

  @GetMapping(ApiConstant.GET_AVG_FARE_HEATMAP)
  public Mono<BaseResponse<List<AverageFareHeatmap>>> getAvgFareHeatmap(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam("date") LocalDate date) {

    return fareService.getAvgFareHeatmap(date)
        .map(BaseResponse::ok);
  }
}
