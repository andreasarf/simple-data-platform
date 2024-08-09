package org.andreasarf.data.platform.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.common.ApiConstant;
import org.andreasarf.data.platform.common.ErrorReason;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.dto.trip.TotalTrip;
import org.andreasarf.data.platform.service.TripService;
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
public class TripController {

  private final TripService tripService;

  @GetMapping(ApiConstant.GET_TOTAL_TRIPS)
  public Mono<ResponseEntity<BaseResponse<List<TotalTrip>>>> getTotalTrips(
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam("start") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam("end") LocalDate endDate) {

    if (endDate.isBefore(startDate)) {
      return Mono.just(ResponseUtil.invalidParam(ErrorReason.END_START_DATE));
    }

    return tripService.getTotalTrips(startDate, endDate)
        .map(ResponseUtil::ok);
  }
}
