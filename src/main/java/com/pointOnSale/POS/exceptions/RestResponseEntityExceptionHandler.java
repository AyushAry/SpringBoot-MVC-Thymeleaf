package com.pointOnSale.POS.exceptions;

import static com.pointOnSale.POS.common.CONSTANTS.EXCEPTION_MESSAGE_NOACCESS;
import static com.pointOnSale.POS.common.CONSTANTS.EXCEPTION_RESOURCE_NOT_FOUND;


import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

import com.pointOnSale.POS.util.CommonUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Log
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  @Autowired
  private CommonUtils commonUtils;

  @ExceptionHandler({ResourceNotFoundException.class})
  public ModelAndView resourceNotFoundException(ResourceNotFoundException e) {
    log.log(Level.SEVERE, e.getMessage(), e);
    ModelAndView mav = new ModelAndView();
    mav.addObject("message", commonUtils.getI18NString(EXCEPTION_RESOURCE_NOT_FOUND));
    mav.addObject("exception", e);
    mav.setViewName("error");
    return mav;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ModelAndView accessDeniedException(AccessDeniedException e) {
    log.log(Level.SEVERE, e.getMessage(), e);
    ModelAndView mav = new ModelAndView();
    mav.addObject("message", commonUtils.getI18NString(EXCEPTION_MESSAGE_NOACCESS));
    mav.addObject("exception", e);
    mav.setViewName("error");
    return mav;
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView otherException(final Exception e) {
    log.log(Level.SEVERE, e.getMessage(), e);
    ModelAndView mav = new ModelAndView();
    mav.addObject("message", commonUtils.getI18NString(EXCEPTION_MESSAGE_UNKNOWN));
    mav.addObject("exception", e);
    mav.setViewName("error");
    return mav;
  }

  @ExceptionHandler({DMCException.class})
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorResponse handleDMCException(HttpServletRequest req, DMCException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ex.getMessage());
  }
}