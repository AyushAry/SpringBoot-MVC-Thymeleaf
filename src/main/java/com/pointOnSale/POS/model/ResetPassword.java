package com.pointOnSale.POS.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
/**
 * Model to handle reset form
 */
public class ResetPassword {
  @NotNull
  private String password;
  @NotNull
  private String confirmPassword;
  private String token;
  private Timestamp timestamp;
}
