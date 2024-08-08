package org.andreasarf.data.platform.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.andreasarf.data.platform.common.enums.ErrorCode;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BaseResponse<T> implements Serializable {

  @JsonIgnore
  private ErrorCode errorCode;
  private List<String> errorReasons;
  private T data;

  public static <T> BaseResponse<T> invalidParam(String reason) {
    return BaseResponse.<T>builder()
        .errorCode(ErrorCode.INVALID_PARAM)
        .errorReason(reason)
        .build();
  }

  @JsonGetter
  public String getCode() {
    return errorCode.getCode();
  }

  @JsonGetter
  public String getMessage() {
    return errorCode.getMessage();
  }

  public static class BaseResponseBuilder<T> {

    public BaseResponseBuilder<T> errorReason(String errorReason) {
      if (this.errorReasons == null) {
        this.errorReasons = new ArrayList<>();
      }
      this.errorReasons.add(errorReason);
      return this;
    }
  }
}
