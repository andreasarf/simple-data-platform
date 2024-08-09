package org.andreasarf.data.platform.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import reactor.test.StepVerifier;

class JacksonMapperTest {

  @Test
  void givenValidObject_whenWriteValueAsString_thenShouldSerialize() {
    // arrange
    final var obj = new Dummy("value");
    final var expected = "{\"key\":\"value\"}";

    // act
    final var res = JacksonMapper.writeValueAsString(obj);

    // assert
    assertEquals(expected, res);
  }

  @Test
  void givenClass_whenReadValue_thenShouldDeserialize() {
    // arrange
    final var json = "{\"key\":\"value\"}";
    final var expected = new Dummy("value");

    // act
    final var res = JacksonMapper.readValue(json, Dummy.class);

    // assert
    assertEquals(expected, res);
  }

  @Test
  void givenClassAndInvalidJson_whenReadValue_thenShouldNull() {
    // arrange
    final var json = "{\"key\":\"value\"";

    // act
    final var res = JacksonMapper.readValue(json, Dummy.class);

    // assert
    assertNull(res);
  }

  @Test
  void givenTypeReference_whenReadValue_thenShouldDeserialize() {
    // arrange
    final var json = "{\"key\":\"value\"}";
    final var expected = new Dummy("value");

    // act
    final var res = JacksonMapper.readValue(json, new TypeReference<Dummy>() {
    });

    // assert
    assertEquals(expected, res);
  }

  @Test
  void givenTypeReferenceAndInvalidJson_whenReadValue_thenShouldDeserialize() {
    // arrange
    final var json = "{\"key:\"value\"}";

    // act
    final var res = JacksonMapper.readValue(json, new TypeReference<Dummy>() {
    });

    // assert
    assertNull(res);
  }

  @Test
  void givenClass_whenReadValueReactive_thenShouldDeserialize() {
    // arrange
    final var json = "{\"key\":\"value\"}";
    final var expected = new Dummy("value");

    // act
    final var res = JacksonMapper.readValueReactive(json, Dummy.class);

    // assert
    StepVerifier.create(res)
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  void givenTypeReference_whenReadValueReactive_thenShouldDeserialize() {
    // arrange
    final var json = "{\"key\":\"value\"}";
    final var expected = new Dummy("value");

    // act
    final var res = JacksonMapper.readValueReactive(json, new TypeReference<Dummy>() {/**/
    });

    // assert
    StepVerifier.create(res)
        .expectNext(expected)
        .verifyComplete();
  }

  @Test
  void whenWriteByte_thenShouldReturnByteArray() {
    // arrange
    final var obj = new Dummy("value");
    final var expected = new byte[]{123, 34, 107, 101, 121, 34, 58, 34, 118, 97, 108, 117, 101, 34,
        125};

    // act
    final var res = JacksonMapper.writeByte(obj);

    // assert
    StepVerifier.create(res)
        .assertNext(actual -> assertThat(actual).isEqualTo(expected))
        .verifyComplete();
  }

  @Test
  void givenValidJson_whenJsonNode_thenShouldReturnJson() throws Exception {
    // arrange
    final var json = "{\"key\":\"value\"}";

    // act
    final var res = JacksonMapper.jsonNode(json);

    // assert
    JSONAssert.assertEquals("{\"key\":\"value\"}", res.toString(), false);
  }

  @Test
  void givenInvalidJson_whenJsonNode_thenShouldReturnJson() {
    // arrange
    final var json = "{\"key\"\"value\"}";

    // act
    final var res = JacksonMapper.jsonNode(json);

    // assert
    assertNull(res);
  }

  @Test
  void givenValidJson_whenIsValid_thenShouldReturnTrue() {
    // arrange
    final var json = "{\"key\":\"value\"}";

    // act
    final var res = JacksonMapper.isValid(json);

    // assert
    assertTrue(res);
  }

  @Test
  void givenInvalidJson_whenIsValid_thenShouldReturnFalse() {
    // arrange
    final var json = "{\"key\":\"value\"";

    // act
    final var res = JacksonMapper.isValid(json);

    // assert
    assertFalse(res);
  }

  @Data
  @AllArgsConstructor
  static class Dummy {

    private String key;
  }
}
