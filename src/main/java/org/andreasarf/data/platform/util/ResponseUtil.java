package org.andreasarf.data.platform.util;

import lombok.experimental.UtilityClass;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseUtil {

  public <T> ResponseEntity<BaseResponse<T>> ok(T data) {
    return ResponseEntity.ok(BaseResponse.ok(data));
  }

  public <T> ResponseEntity<BaseResponse<T>> invalidParam(String reason) {
    return ResponseEntity.badRequest()
        .body(BaseResponse.invalidParam(reason));
  }

  public <T> ResponseEntity<BaseResponse<T>> internalError(String reason) {
    return ResponseEntity.internalServerError()
        .body(BaseResponse.internalError(reason));
  }
}
