package com.pointOnSale.POS.model;

import java.util.List;

import com.embeddigital.domain.*;
import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * SearchResponseModel provides a generic model for listing and search related objects
 *
 * @param <T>
 */
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponseModel<T> {
  private List<T> results;
  private Boolean isFirst;
  private Boolean isLast;
  private Integer size;
  private Integer currentPage;
  private Integer totalPages;
  private String sortedBy;
  private String sortedDirection;
  private String filters;
  private Brand brand;
  private Location location;
  private Display display;
  private Store store;
  private Zone zone;
  private Section section;
  private SubSection subSection;
  private Principal principal;
  private ItemCategory itemCategory;
}
