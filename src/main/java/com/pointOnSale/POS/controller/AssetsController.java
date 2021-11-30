package com.pointOnSale.POS.controller;

import static com.pointOnSale.POS.common.CONSTANTS.BREADCRUMB_ASSETS_KEY;
import static com.pointOnSale.POS.common.CONSTANTS.BREADCRUMB_HOME_KEY;
import static com.pointOnSale.POS.common.CONSTANTS.BREADCRUMB_SELECT_BRAND_KEY;
import static com.pointOnSale.POS.common.CONSTANTS.ENTITY_FRAGMENT_ASSETS;
import static com.pointOnSale.POS.common.CONSTANTS.REQUIRED_FIELDS_IMAGE_INVALID;
import static com.pointOnSale.POS.common.CONSTANTS.REQUIRED_FIELDS_VIDEO_INVALID;
import static com.pointOnSale.POS.common.enums.AssetTypeEnum.IMAGE;

import com.pointOnSale.POS.common.enums.AssetTypeEnum;
import com.pointOnSale.POS.domain.Asset;
import com.pointOnSale.POS.domain.Brand;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Log
public class AssetsController extends DMCAbstractController {

  @Autowired
  private AuthUserUtil authUserUtil;

  @Autowired
  private AssetsService assetsService;

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private BrandService brandService;

  @Autowired
  private S3Service s3Service;


  @GetMapping(path = "/assets/home")
  @PreAuthorize("@accessSecurityService.doesUserHaveAccessToFilter(#filters)")
  public ModelAndView listAssets(@RequestParam("size") Optional<Integer> size,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("sortedBy") Optional<String> sortedBy,
                                 @RequestParam("sortedDirection")
                                     Optional<Sort.Direction> sortedDirection,
                                 @RequestParam("filters") Optional<String> filters) {
    ModelAndView modelAndView = new ModelAndView("menu/asset/list");
    // Ensure access for permitted objects
    if (!filters.isPresent()) {
      filters = createDefaultFilterForUser();
    }
    size = Optional.of(Integer.MAX_VALUE);
    page = Optional.of(0);
    SearchResponseModel<Asset> assets =
        assetsService.list(page, size, sortedBy, sortedDirection, filters);
    modelAndView.addObject("model", assets);
    modelAndView.addObject("listHome", "assets");
    modelAndView.addObject(
        "breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(
                commonUtils.getI18NString(BREADCRUMB_HOME_KEY), true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_ASSETS_KEY), false, false,
                null, null, null)
        });
    // Set filter in session
    if (filters.isPresent()) {
      setSessionFilter(filters.get());
    }
    setListingHome(ENTITY_FRAGMENT_ASSETS);
    return modelAndView;
  }


  @GetMapping(path = "/assets/new")
  public ModelAndView createAsset(@RequestParam("brandId") Optional<Long> brandId) {
    if (brandId.isPresent()) {
      return createBrand(brandId.get());
    } else {
      ModelAndView modelAndView = new ModelAndView("menu/asset/select-brand");
      modelAndView.addObject("entityHome", commonUtils.getI18NString(BREADCRUMB_SELECT_BRAND_KEY));
      modelAndView.addObject("asset", new Asset());
      modelAndView.addObject("brands", brandService.listAllBrandsForUser());
      return modelAndView;
    }
  }

  private ModelAndView createBrand(Long brandId) {
    ModelAndView modelAndView = new ModelAndView("menu/asset/entity");
    log.info("Current User is =>" + authUserUtil.getPrincipalId());
    Asset asset = assetsService.initialiseNewAsset(brandId);
    modelAndView.addObject("entityHome", "Create Assets");
    modelAndView.addObject("asset", asset);
    modelAndView
        .addObject("types", Arrays.asList(IMAGE.toString(), AssetTypeEnum.VIDEO.toString()));
    modelAndView.addObject(
        "breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(
                commonUtils.getI18NString(BREADCRUMB_HOME_KEY), true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_ASSETS_KEY), false, false,
                null, null, null)
        });

    return modelAndView;
  }

  @PostMapping(path = "/assets/version/add")
  @ResponseStatus(HttpStatus.OK)
  public void uploadAssetVersion(Long id,
                                 @RequestParam("uploadFile") MultipartFile multipartFile)
      throws Exception {
    assetsService.addAssetVersion(id, multipartFile);
  }

  @PostMapping(path = "/assets", params = "action=save")
  public String saveAsset(@Valid Asset asset,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
    try {
      assetsService.persist(asset);
    } catch (Exception e) {
      String message = e.getMessage();
      if (StringUtils.isNotBlank(message)) {
        if (message.equals(REQUIRED_FIELDS_IMAGE_INVALID)) {
          redirectAttributes.addFlashAttribute("errorimage",
              commonUtils.getI18NString(REQUIRED_FIELDS_IMAGE_INVALID));
        } else if (message.equals(REQUIRED_FIELDS_VIDEO_INVALID)) {
          redirectAttributes.addFlashAttribute("error",
              commonUtils.getI18NString(REQUIRED_FIELDS_VIDEO_INVALID));
        } else {
          redirectAttributes.addFlashAttribute("othererror", message);
        }
      }
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.asset", bindingResult);
      redirectAttributes.addFlashAttribute("asset", asset);
      return "redirect:/assets/?action=edit";
    }
    return "redirect:/assets/home?filters=" + getSessionFilters();
  }

  @GetMapping(path = "/assets", params = "action=edit")
  public ModelAndView editAssets(@RequestParam("id") Optional<Long> id, Model model) {
    ModelAndView modelAndView = null;
    Asset asset = null;
    if (model != null && !id.isPresent()) {
      asset = (Asset) model.asMap().get("asset");
      modelAndView = new ModelAndView("menu/asset/entity");
      modelAndView.addObject("entityHome", "Edit Asset");
      modelAndView.addObject("asset", asset);
    }
    if (id.isPresent()) {
      asset = assetsService.getAsset(id.get());
      modelAndView = new ModelAndView("menu/asset/entity");
      modelAndView.addObject("entityHome", "Edit Asset");
      modelAndView.addObject("asset", asset);
    }
    modelAndView
        .addObject("types", Arrays.asList(IMAGE.toString(), AssetTypeEnum.VIDEO.toString()));
    modelAndView.addObject(
        "breadcrumbs",
        new BreadCrumbModel[] {
            new BreadCrumbModel(
                commonUtils.getI18NString(BREADCRUMB_HOME_KEY), true, true, null, null, null),
            new BreadCrumbModel(commonUtils.getI18NString(BREADCRUMB_ASSETS_KEY), false, false,
                null, null, null),
            new BreadCrumbModel(asset.getName(), false, false, null, null, null)
        });
    return modelAndView;
  }

  /**
   * Cancel operation
   *
   * @return
   */
  @PostMapping(path = "/assets", params = "action=cancel")
  public String cancelAssetsEdit() {
    return "redirect:/" + getListingHome(ENTITY_FRAGMENT_ASSETS) + "/home?filters=" +
        getSessionFilters();
  }

  /**
   * Delete
   *
   * @param id
   * @return
   */
  @DeleteMapping(path = "/assets/{id}")
  public String deleteAssets(@PathVariable(name = "id") Long id) {
    try {
      assetsService.deleteById(id);
    } catch (Exception e) {
      String message = e.getMessage();
      return "redirect:/linkedAssetsList?id="+id;
    }
    return cancelAssetsEdit();
  }


  @PostMapping(path = "/assets/brandselect", params = "action=save")
  public String saveAssetsBrandSelect(Brand brand) {
    return "redirect:/assets/new?brandId=" + brand.getBrandId();
  }

  @PostMapping(path = "/assets/brandselect", params = "action=cancel")
  public String cancelAssetsBrandSelect() {
    return "redirect:/assets/home?filters=" + getSessionFilters();
  }

  @PostMapping(path = "/assets/updateVersion/target/{target}/source/{source}/id/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updateVersion(@PathVariable(name = "target") String target,
                            @PathVariable(name = "source") String source,
                            @PathVariable(name = "id") Long id) {
    assetsService.updateAssetVersionByEntity(target, source, id);
  }

  @GetMapping(path = "/linkedAssetsList")
  public ModelAndView linkedAssetsList(@RequestParam("id") Long id) {
    ModelAndView modelAndView = new ModelAndView("menu/asset/linkedAssetsList");
    List<EntityModel> entityModels = assetsService.checkAssetInEntity(id);
    modelAndView.addObject("entityModels",entityModels);
    return modelAndView;
  }

  @GetMapping(path = "/linkedAssetsList", params = "action=cancel")
  public String cancelSelect() {
    return "redirect:/assets/home?filters=" + getSessionFilters();
  }
}
