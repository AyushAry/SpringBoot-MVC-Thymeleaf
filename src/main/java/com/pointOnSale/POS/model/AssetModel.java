package com.pointOnSale.POS.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class AssetModel {
  @ToString.Include
  @EqualsAndHashCode.Include
  private Long id;
  @ToString.Include
  private String name;
  @ToString.Include
  private String type;
  @ToString.Include
  private String src;
  private Long height;
  private Long width;
}
