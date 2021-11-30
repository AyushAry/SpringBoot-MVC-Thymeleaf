package com.pointOnSale.POS.domain.repositories;

import com.embeddigital.domain.Brand;
import com.embeddigital.domain.support.IModelRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * This provides data access methods for Brands
 */
public interface BrandRepository extends IModelRepo, JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

  /**
   * Filter by brands
   *
   * @param brandId
   * @return
   */
  static Specification<Brand> equalsBrand(Long brandId) {
    return (brand, cq, cb) -> cb.equal(brand.get("brandId"), brandId);
  }

  @Query("SELECT COALESCE(MAX(number),0) FROM Brand")
  Integer getMaxNumber();

  Optional<Brand> findByConceptName(String conceptName);

  List<Brand> findByAddressState(String state);

  Optional<Brand> findByNumber(int number);

  Optional<Brand> findByName(String brandName);



}
