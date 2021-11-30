package com.pointOnSale.POS.domain.support;

import org.springframework.data.jpa.domain.Specification;

/**
 * This interface extends repositories to enable criteria based search
 *
 * @author Kedar (kedar@etasens.com)
 */
public interface IModelRepo {
  public default <T> Specification<T> setSpecification(Specification<T> specification, Specification<T> spec) {
    if (specification == null) {
      specification = spec;
    } else {
      specification = specification.and(spec);
    }
    return specification;
  }
}
