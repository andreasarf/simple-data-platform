package org.andreasarf.data.platform.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.andreasarf.data.platform.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public Mono<ResponseEntity<BaseResponse<Void>>> handleResponseStatusException(
      ResponseStatusException ex, ServerWebExchange exchange) {
    log.warn("A response status error occurred", ex);
    return Mono.just(ResponseUtil.invalidParam(ex.getReason()));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<BaseResponse<Void>>> handleGenericException(Exception ex,
      ServerWebExchange exchange) {
    log.error("An unexpected error occurred", ex);
    return Mono.just(ResponseUtil.internalError("An unexpected error occurred: " + ex.getMessage()));
  }
}
