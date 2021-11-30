package com.pointOnSale.POS.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The model for sending emails
 */
@Data
@Builder
public class Mail {
  private String from;
  private String to;
  private String[] toList;
  private String subject;
  private Map<String, Object> model;
}
