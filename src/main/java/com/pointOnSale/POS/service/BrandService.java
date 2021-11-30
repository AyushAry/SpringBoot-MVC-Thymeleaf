package com.pointOnSale.POS.service;

import static com.embeddigital.common.CONSTANTS.ALL_DAY;

import com.embeddigital.domain.Brand;
import com.embeddigital.domain.DisplayGroups;
import com.embeddigital.domain.Location;
import com.embeddigital.domain.repositories.BrandRepository;
import com.embeddigital.domain.support.RoleTypeEnum;
import com.embeddigital.model.SearchResponseModel;
import com.embeddigital.util.AuthUserUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BrandService provides functions related to Brands
 *
 * @author Kedar (kedar@etasens.com)
 */
@Log
@Service
@Transactional
public class BrandService extends DMCAbstractListService<Brand> {

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private DisplayGroupsService groupsService;

  @Autowired
  private AuthUserUtil authUserUtil;

  @Autowired
  private MailService mailService;

  private final String DEFAULT_SORT_BY = "number";
  private final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

  /**
   * Return the repository reference
   *
   * @return
   */
  @Override
  protected JpaSpecificationExecutor getRepository() {
    return brandRepository;
  }

  /**
   * Get the preferred sort options
   *
   * @return
   */
  @Override
  protected Sort getSortOptions() {
    return null;
  }

  /**
   * Return the default sort direction
   *
   * @return
   */
  @Override
  protected Sort.Direction getDefaultSortDirection() {
    return DEFAULT_SORT_DIRECTION;
  }

  /**
   * Adds additional filter specific to list
   *
   * @param filters
   * @return
   */
  @Override
  protected Specification addFilter(List<DMCListFilter> filters) {
    Specification specification = null;
    if (filters != null && !filters.isEmpty()) {
      for (DMCListFilter filter : filters) {
        switch (filter.getField()) {
          case "brand":
            try {
              specification =
                  brandRepository.setSpecification(
                      specification,
                      BrandRepository.equalsBrand(Long.valueOf(filter.getFieldValue().toString())));
            } catch (NumberFormatException nfe) {
              log.log(Level.SEVERE, "Not able to parse ", nfe);
            }
            break;
        }
      }
    }
    return specification;
  }

  /**
   * Return the default sort by
   *
   * @return
   */
  @Override
  protected String getDefaultSortBy() {
    return DEFAULT_SORT_BY;
  }

  /**
   * Post process search results by specific services
   *
   * @param filters
   * @param searchResponseModel
   */
  @Override
  protected void postProcessSearchResponse(
      List<DMCListFilter> filters, SearchResponseModel<Brand> searchResponseModel) {
  }

  /**
   * Persist function for a Brand
   *
   * @param brand
   * @return
   */
  public Brand persistBrand(Brand brand) {
    // Check if pos disable mail needs to be sent
    if (Objects.nonNull(brand.getBrandId())) {
      if (Objects.isNull(brand.getPosSystem()) || brand.getPosSystem().isEmpty()  ) {
        Brand brandFromDb = brandRepository.getOne(brand.getBrandId());
        if (Objects.nonNull(brandFromDb.getPosSystem()) && !brandFromDb.getPosSystem().isEmpty()) {
          mailService.sendPosDisabledEmail("Brand", brandFromDb.getNumber().toString());
        }
      }
    }

    if (Objects.isNull(brand.getBrandId())) {
      DisplayGroups group = new DisplayGroups();
      group.setName(ALL_DAY);
      group.setNumber(0);
      group.setBrand(brand);
      brand.setGroups(Arrays.asList(group));
      groupsService.save(group);
    }
    return brandRepository.save(brand);
  }

  /**
   * Initialise a new Brand
   *
   * @return
   */
  public Brand initialiseNewBrand() {
    Brand brand = new Brand();
    brand.setNumber(brandRepository.getMaxNumber() + 1);
    return brand;
  }

  /**
   * Get brand by id
   *
   * @param brandId
   * @return
   */
  public Brand getBrand(Long brandId) {
    return brandRepository.getOne(brandId);
  }


  /**
   * Lists of all brands for user
   *
   * @return
   */
  public List<Brand> listAllBrandsForUser() {
    if (authUserUtil.hasRole(RoleTypeEnum.Role.SUPER_ADMIN)) {
      return brandRepository.findAll();
    } else {
      return Arrays.asList(authUserUtil.getPrincipal().getBrand());
    }
  }

  /**
   * get Location list
   */
  public List<Location> getLocationList(long brandId) {
    Brand brand = brandRepository.getOne(brandId);
    List<Location> locations = brand.getLocations();
    return locations;
  }

  /**
   * Delete by id
   *
   * @param id
   */
  public void deleteById(Long id) {
    brandRepository.deleteById(id);
  }


}
