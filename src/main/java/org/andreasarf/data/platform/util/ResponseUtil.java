package org.andreasarf.data.platform.util;

import lombok.experimental.UtilityClass;
import org.andreasarf.data.platform.dto.BaseResponse;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseUtil {

  public <T> ResponseEntity<BaseResponse<T>> invalidParam(String reason) {
    return ResponseEntity.badRequest()
        .body(BaseResponse.invalidParam(reason));
  }
}
