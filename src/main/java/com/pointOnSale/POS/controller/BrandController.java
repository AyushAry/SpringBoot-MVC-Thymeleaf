package com.pointOnSale.POS.controller;


import com.pointOnSale.POS.domain.Brand;
import com.pointOnSale.POS.model.BreadCrumbModel;
import com.pointOnSale.POS.model.SearchResponseModel;
import com.pointOnSale.POS.service.BrandService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static com.pointOnSale.POS.common.CONSTANTS.*;
import static com.pointOnSale.POS.common.CONSTANTS.BREADCRUMB_BRANDS_KEY;
import static com.pointOnSale.POS.common.CONSTANTS.BREADCRUMB_HOME_KEY;
import static com.pointOnSale.POS.common.CONSTANTS.ENTITY_FRAGMENT_BRANDS;

@Log
@Controller
public class BrandController extends DMCAbstractController {

  @Autowired
  private UtilityService utilityService;

  @Autowired
  private BrandService brandsService;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private IndustryService industryService;

  @Autowired
  private PosSystemService posSystemService;

  @GetMapping(path = "/brands/home")
  @PreAuthorize("@accessSecurityService.doesUserHaveAccessToFilter(#filters)")
  public ModelAndView listBrands(@RequestParam("size") Optional<Integer> size,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("sortedBy") Optional<String> sortedBy,
                                 @RequestParam("sortedDirection") Optional<Sort.Direction> sortedDirection,
                                 @RequestParam("filters") Optional<String> filters) {
    ModelAndView modelAndView = new ModelAndView("menu/brands/list");
    // Ensure access for permitted objects
    if (!filters.isPresent()) {
      filters = createDefaultFilterForUser();
    }
    size = Optional.of(Integer.MAX_VALUE);
    page = Optional.of(0);
    SearchResponseModel<Brand> brands = brandsService.list(page, size, sortedBy, sortedDirection, filters);
    modelAndView.addObject("model", brands);
    modelAndView.addObject("listHome", "brands");
    modelAndView.addObject("breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_HOME_KEY),
                true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_BRANDS_KEY),
                false, false, null, null, null)});
    // Set filter in session
    if (filters.isPresent()) {
      setSessionFilter(filters.get());
    }
    setListingHome(ENTITY_FRAGMENT_BRANDS);
    return modelAndView;
  }

  @GetMapping(path = "/brands/new")
  public ModelAndView createBrand() {
    ModelAndView modelAndView = new ModelAndView("menu/brands/entity");
    modelAndView.addObject("entityHome", "Create Brand");
    modelAndView.addObject("brand",
        brandsService.initialiseNewBrand());
    modelAndView.addObject("states", utilityService.getDefaultStates());
    modelAndView.addObject("countries", utilityService.getCountries());
    modelAndView.addObject("industries",industryService.listIndustry());
    modelAndView.addObject("posEnable",posSystemService.findAll());
    modelAndView.addObject("breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_HOME_KEY),
                true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_BRANDS_KEY),
                false, false, null, null, null)});
    return modelAndView;
  }

  @PostMapping(path = "/brands", params = "action=save")
  public String saveBrand(Brand brand) {
    brandsService.persistBrand(brand);
    return cancelBrandEdit();
  }

  @GetMapping(path = "/brands", params = "action=edit")
  @PreAuthorize("@accessSecurityService.doesUserHaveEditAccessToEntity(#brandId, 'BRAND')")
  public ModelAndView editBrand(@RequestParam("brandId") Long brandId) {
    Brand brand = brandsService.getBrand(brandId);
    ModelAndView modelAndView = new ModelAndView("menu/brands/entity");
    modelAndView.addObject("entityHome", "Edit Brand");
    modelAndView.addObject("brand", brand);
    modelAndView.addObject("states", utilityService.getStatesByCountry(
        brand.getAddress().getCountry()));
    modelAndView.addObject("countries", utilityService.getCountries());
    modelAndView.addObject("industries",industryService.listIndustry());
    modelAndView.addObject("posEnable",posSystemService.findAll());
    modelAndView.addObject("breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_HOME_KEY),
                true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_BRANDS_KEY),
                false, false, null, null, null),
            new BreadCrumbModel(brand.getName(),
                false, false, null, null, null)});
    return modelAndView;
  }

  /**
   * Cancel operation
   *
   * @return
   */
  @PostMapping(path = "/brands", params = "action=cancel")
  public String cancelBrandEdit() {
    return "redirect:/" + getListingHome(ENTITY_FRAGMENT_BRANDS) + "/home";
  }

  /**
   * Delete
   *
   * @param id
   * @return
   */
  @DeleteMapping(path = "/brands/{id}")
  public String deleteBrand(@PathVariable(name = "id") Long id) {
    brandsService.deleteById(id);
    return cancelBrandEdit();
  }

}
