package com.pointOnSale.POS.domain.repositories;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AssetRepository extends IModelRepo, JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {

  static Specification<Asset> equalsAssets(Long id) {
    return (asset, cq, cb) -> cb.equal(asset.get("id"), id);
  }

  /**
   * Method to return all assets for a given brand
   *
   * @param brand - Brand to filter by
   * @return List of {@link Asset}
   */
  List<Asset> findAllByBrand(Brand brand);

  Optional<Asset> findByName(String name);
}
