package com.pointOnSale.POS.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "status", "message"})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
  @JsonProperty("code")
  private Integer code;
  @JsonProperty("status")
  private String status;
  @JsonProperty("message")
  private String message;
}
