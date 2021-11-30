package com.pointOnSale.POS.service;

import static com.embeddigital.common.CONSTANTS.BRAND_NOT_FOUND;
import static com.embeddigital.common.CONSTANTS.REQUIRED_FIELDS_IMAGE_INVALID;
import static com.embeddigital.common.CONSTANTS.REQUIRED_FIELDS_VIDEO_INVALID;
import static com.embeddigital.common.CONSTANTS.UPDATE_REASON_CODE_RELOAD;

import com.embeddigital.common.enums.AssetTypeEnum;
import com.embeddigital.domain.Asset;
import com.embeddigital.domain.AssetVersion;
import com.embeddigital.domain.Brand;
import com.embeddigital.domain.Display;
import com.embeddigital.domain.Item;
import com.embeddigital.domain.Location;
import com.embeddigital.domain.Section;
import com.embeddigital.domain.Store;
import com.embeddigital.domain.Zone;
import com.embeddigital.domain.repositories.AssetRepository;
import com.embeddigital.domain.repositories.AssetVersionRepository;
import com.embeddigital.domain.repositories.BrandRepository;
import com.embeddigital.domain.repositories.DisplayRepository;
import com.embeddigital.domain.repositories.ItemRepository;
import com.embeddigital.domain.repositories.LocationRepository;
import com.embeddigital.domain.repositories.SectionRepository;
import com.embeddigital.domain.repositories.StoreRepository;
import com.embeddigital.domain.repositories.ZoneRepository;
import com.embeddigital.model.EntityModel;
import com.embeddigital.model.SearchResponseModel;
import com.embeddigital.service.s3.S3Service;
import com.embeddigital.util.CommonUtils;
import com.embeddigital.web.exceptions.DMCException;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * AssetsService provides Functions for  managing image files and video files in the system
 *
 * @author Kedar (kedar@etasens.com)
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AssetsService extends DMCAbstractListService<Asset> {

  @Autowired
  private CommonUtils commonUtils;

  @Autowired
  private AssetRepository assetRepository;

  @Autowired
  private AssetVersionRepository assetVersionRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private DisplayRepository displayRepository;

  @Autowired
  private ZoneRepository zoneRepository;

  @Autowired
  private SectionRepository sectionRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private S3Service s3Service;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private StoreRepository storeRepository;

  private final String DEFAULT_SORT_BY = "id";
  private final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

  @Override
  protected JpaSpecificationExecutor getRepository() {
    return assetRepository;
  }

  @Override
  protected Sort getSortOptions() {
    return null;
  }

  @Override
  protected Sort.Direction getDefaultSortDirection() {
    return DEFAULT_SORT_DIRECTION;
  }

  @Override
  protected Specification addFilter(List<DMCListFilter> filters) {
    Specification specification = null;
    if (filters != null && !filters.isEmpty()) {
      for (DMCListFilter filter : filters) {
        switch (filter.getField()) {
          case "Asset":
            try {
              specification =
                  assetRepository.setSpecification(
                      specification,
                      AssetRepository.equalsAssets(
                          Long.valueOf(filter.getFieldValue().toString())));
            } catch (NumberFormatException nfe) {
              log.error("Not able to parse ", nfe);
            }
            break;
        }
      }
    }
    return specification;
  }

  @Override
  protected String getDefaultSortBy() {
    return DEFAULT_SORT_BY;
  }

  @Override
  protected void postProcessSearchResponse(
      List<DMCListFilter> filters, SearchResponseModel<Asset> searchResponseModel) {
  }

  public Asset getAsset(Long id) {
    return assetRepository.getOne(id);
  }

  public Asset initialiseNewAsset(Long brandId) {
    Asset asset = new Asset();
    asset.setBrand(brandRepository.getOne(brandId));
    return asset;
  }

  public Asset persist(Asset asset)
      throws Exception {
    Long brandId = asset.getBrand().getBrandId();
    Brand brand =
        brandRepository
            .findById(asset.getBrand().getBrandId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        MessageFormat.format(
                            BRAND_NOT_FOUND, new Object[] {brandId})));
    asset.setBrand(brand);
    return assetRepository.save(asset); // Save to get a id
  }

  public void deleteById(Long id) {
    assetRepository.deleteById(id);
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  /**
   * Get all assets for brand
   *
   * @param brand
   * @return
   */
  public List<Asset> getAssetsByBrand(Brand brand) {
    return assetRepository.findAllByBrand(brand);
  }

  /**
   * Create a version for the asset with given id
   *
   * @param id
   * @param multipartFile
   */
  public void addAssetVersion(Long id, MultipartFile multipartFile) throws Exception {
    Asset asset = assetRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Asset Not found"));
    int version = Objects.nonNull(asset.getVersions()) ? asset.getVersions().size() + 1 : 1;
    AssetVersion assetVersion = new AssetVersion();
    assetVersion.setVersion(version);
    String contentType = multipartFile.getContentType();
    if (asset.getType().equals(AssetTypeEnum.IMAGE.toString()) &&
        contentType.startsWith("video")) {
      throw new DMCException(commonUtils.getI18NString(REQUIRED_FIELDS_IMAGE_INVALID));
    } else if (asset.getType().equals(AssetTypeEnum.VIDEO.toString()) &&
        contentType.startsWith("image")) {
      throw new DMCException(commonUtils.getI18NString(REQUIRED_FIELDS_VIDEO_INVALID));
    }
    File file = null;
    if (asset.getType().equals(AssetTypeEnum.IMAGE.toString())) {
      file = convertMultiPartToFile(multipartFile);
      BufferedImage bufferedImage = ImageIO.read(file);
      assetVersion.setWidth(Long.valueOf(bufferedImage.getWidth()));
      assetVersion.setHeight(Long.valueOf(bufferedImage.getHeight()));
    } else if (asset.getType().equals(AssetTypeEnum.VIDEO.toString())) {
      try {
        file = convertMultiPartToFile(multipartFile);
        IContainer container = IContainer.make();
        int result = container.open(String.valueOf(file), IContainer.Type.READ, null);
        if (result < 0) {
          throw new RuntimeException("Failed to open media file");
        }
        int numStreams = container.getNumStreams();
        for (int i = 0; i < numStreams; i++) {
          IStream stream = container.getStream(i);
          IStreamCoder coder = stream.getStreamCoder();
          if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
            assetVersion.setWidth((long) coder.getWidth());
            assetVersion.setHeight((long) coder.getHeight());
          }
        }
      } catch (Exception e) {
        log.error("Error Processing video", e);
        throw e;
      }
    }

    String md5 = DigestUtils.md5DigestAsHex(FileUtils.readFileToByteArray(file));

    String key = "assets/brand/" + asset.getBrand().getNumber() + "/" + asset.getType() + "/" + md5;

    // use hashmap
    String src = s3Service.getSrcForKey(key);
    assetVersion.setSrc(src);
    assetVersion.setS3Key(key);

    // Check if file exists
    AssetVersion previousVersion = assetVersionRepository.findByS3Key(key);
    if (Objects.nonNull(previousVersion)) {
      throw new DMCException("Same asset exists as " + asset.getName() + " " +
          "(Version:" + previousVersion.getVersion() + ")");
    }

    // Upload and persist
    if (Objects.nonNull(file) && file.exists()) {
      try {
        s3Service.uploadFileToS3(file, key, true);
        // Add version
        assetVersion.setAsset(asset);
        List<AssetVersion> assetVersions = (Objects.isNull(asset.getVersions())) ?
            new LinkedList<>() : asset.getVersions();
        assetVersions.add(assetVersion);
        asset.setVersions(assetVersions);
        assetVersionRepository.save(assetVersion);
      } catch (Exception e) {
        log.error("Error Uploading file", e);
        throw e;
      } finally {
        file.delete();
      }
    }
  }

  /**
   * Update Asset Version By Entity
   *
   * @param target
   * @param source
   * @param sourceId
   */
  public void updateAssetVersionByEntity(String target, String source, Long sourceId) {
    switch (target) {
      case "display":
        List<Display> displayList = new LinkedList<>();
        switch (source) {
          case "brand":
            Brand brand = brandRepository.getOne(sourceId);
            for (Store store : brand.getStores()) {
              if (Objects.nonNull(store.getDisplays()) && !store.getDisplays().isEmpty()) {
                displayList.addAll(store.getDisplays());
              }
            }
            break;
          case "location":
            Location location = locationRepository.getOne(sourceId);
            for (Store store : location.getStores()) {
              List<Display> displays = store.getDisplays();
              if (Objects.nonNull(displays) && !displays.isEmpty()) {
                displayList.addAll(displays);
              }
            }
            break;
          case "store":
            Store store = storeRepository.getOne(sourceId);
            if (Objects.nonNull(store.getDisplays()) && !store.getDisplays().isEmpty()) {
              displayList.addAll(store.getDisplays());
            }
            break;
          case "display":
            Display display = displayRepository.getOne(sourceId);
            if (Objects.nonNull(display)) {
              displayList.add(display);
            }
        }

        // Process Displays
        for (Display display : displayList) {
          Asset asset = display.getAsset();
          if (Objects.nonNull(asset)) {
            int maxVersion = asset.getVersions().size();
            if (display.getAssetVersion() != maxVersion) {
              display.setAssetVersion(maxVersion);
              display.setRefreshReasonCode(UPDATE_REASON_CODE_RELOAD);
              display.setLastUpdatedOn(new Date());
            }
          }
        }
        displayRepository.saveAll(displayList);
        break;
    }
  }

  public List<EntityModel> checkAssetInEntity(Long id) {
    List<EntityModel> entityModels = new LinkedList<>();
    Optional<Asset> assetOptional = assetRepository.findById(id);
    if (assetOptional.isPresent()) {
      Asset asset = assetOptional.get();
      List<Display> displays = displayRepository.findAllByAsset(asset);
      for (Display display : displays) {
        entityModels.add(new EntityModel(display.getDisplayId(), "Display"));
      }
      List<Zone> zones = zoneRepository.findAllByAsset(asset);
      for (Zone zone : zones) {
        entityModels.add(new EntityModel(zone.getZoneId(), "Zone"));
      }

      List<Section> sections = sectionRepository.findAllByAsset(asset);
      for (Section section : sections) {
        entityModels.add(new EntityModel(section.getSectionId(), "Section"));
      }

      List<Item> items = itemRepository.findAllByAsset(asset);
      for (Item item : items) {
        entityModels.add(new EntityModel(item.getItemId(), "Item"));
      }
    }
    return entityModels;
  }
}
