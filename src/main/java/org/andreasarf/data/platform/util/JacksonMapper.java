package org.andreasarf.data.platform.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

@Slf4j
@UtilityClass
public class JacksonMapper {

  public static final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

  public static @Nullable String writeValueAsString(Object o) {
    try {
      return objectMapper.writeValueAsString(o);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return null;
    }
  }

  public static <T> T readValue(String content, Class<T> obj) {
    try {
      return objectMapper.readValue(content, obj);
    } catch (Exception e) {
      log.debug("JacksonMapper.readValue() with: {}", e.getMessage(), e);
      return null;
    }
  }

  public static <T> T readValue(String content, TypeReference<T> obj) {
    try {
      return objectMapper.readValue(content, obj);
    } catch (Exception e) {
      log.debug("JacksonMapper.readValue() with: {}", e.getMessage(), e);
      return null;
    }
  }

  public static <T> Mono<T> readValueReactive(String content, Class<T> obj) {
    return Mono.fromCallable(() -> objectMapper.readValue(content, obj));
  }

  public static <T> Mono<T> readValueReactive(String content, TypeReference<T> obj) {
    return Mono.fromCallable(() -> objectMapper.readValue(content, obj));
  }

  /**
   * @param object object
   * @return byte[] value of object or 0 byte if error
   */
  public static Mono<byte[]> writeByte(Object object) {
    return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(object))
        .doOnError(
            throwable -> log.error("JacksonMapper.writeByte() with: " + throwable.getMessage()))
        .onErrorReturn(new byte[0]);
  }

  public static JsonNode jsonNode(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (Exception e) {
      log.error("error parse instruction " + json);
      return null;
    }
  }

  public boolean isValid(String json) {
    try {
      objectMapper.readTree(json);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
