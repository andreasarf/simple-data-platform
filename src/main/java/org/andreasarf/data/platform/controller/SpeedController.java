package org.andreasarf.data.platform.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.common.ApiConstant;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.dto.speed.AverageSpeed;
import org.andreasarf.data.platform.service.SpeedService;
import org.andreasarf.data.platform.util.ResponseUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstant.V1_ROOT)
public class SpeedController {

  private final SpeedService speedService;

  @GetMapping(ApiConstant.GET_AVG_SPEED_24H)
  public Mono<ResponseEntity<BaseResponse<AverageSpeed>>> getAvgSpeedIn24Hours(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam("date") LocalDate date) {

    return speedService.getAvgSpeedIn24Hours(date)
        .map(ResponseUtil::ok);
  }
}
