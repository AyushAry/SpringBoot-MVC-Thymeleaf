package com.pointOnSale.POS.util;

import com.embeddigital.common.CONSTANTS;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.extern.java.Log;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Provides common utility functions
 *
 * @author Kedar (kedar@etasens.com)
 */
@Component
@Log
public class CommonUtils {

  @Autowired
  private MessageSource messageSource;

  private static DateTimeFormatter isoTimeFormat = DateTimeFormatter.ofPattern("hh.mm a");

  /**
   * Get the i18n string from setup messages
   *
   * @param key
   * @return
   */
  public String getI18NString(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());

  }

  /**
   * Validate if email has valid format
   *
   * @param email
   * @return
   */
  public boolean validateEmail(String email) {
    EmailValidator validator = EmailValidator.getInstance();
    return validator.isValid(email);
  }


  /**
   * Utility to get time in ISO form
   *
   * @param time
   * @return
   */
  public static String getTimeAsISO(LocalTime time) {
    return Objects.nonNull(time) ? time.format(isoTimeFormat) : CONSTANTS.EMPTY;
  }
}
