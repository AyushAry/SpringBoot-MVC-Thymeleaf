package com.pointOnSale.POS.common;

import java.text.SimpleDateFormat;

public class CONSTANTS {

  public static final String ROLE_AUTHORITY = "ROLE_{0}";
  public static final String REMEMBER_ME_KEY = "ASERBE22OYBXSQ8SB9WV";

  public static final String BRAND_NOT_FOUND = "Brand Not Found {0}";
  public static final String PRINCIPAL_NOT_FOUND = "User Not Found {0}";

  public static final Integer DEFAULT_LIST_ITEMS_SIZE = 10;
  public static final String FILTERS_KEY = "filters";
  public static final String LISTING_HOME_KEY = "listingHome";
  public static final String ALL_DAY = "ALLDAYS";
  public static final String SINGLE = "Single";

  // Breadcrumb property keys
  public static final String BREADCRUMB_HOME_KEY = "breadcrumb.home";
  public static final String BREADCRUMB_ASSETS_KEY = "breadcrumb.assets";
  public static final String BREADCRUMB_BRANDS_KEY = "breadcrumb.brands";
  public static final String BREADCRUMB_LOCATIONS_KEY = "breadcrumb.locations";
  public static final String BREADCRUMB_CREATELOCATION_KEY = "breadcrumb.createlocation";
  public static final String BREADCRUMB_EDITLOCATION_KEY = "breadcrumb.editlocation";
  public static final String BREADCRUMB_SELECT_BRAND_KEY = "breadcrumb.selectbrand";
  public static final String BREADCRUMB_STORES_KEY = "breadcrumb.stores";
  public static final String BREADCRUMB_CREATESTORE_KEY = "breadcrumb.createstore";
  public static final String BREADCRUMB_EDITSTORE_KEY = "breadcrumb.editstore";
  public static final String BREADCRUMB_DISPLAYS_KEY = "breadcrumb.displays";
  public static final String BREADCRUMB_SELECTDISPLAYS_KEY = "breadcrumb.selectdisplays";
  public static final String BREADCRUMB_SELECTZONES_KEY = "breadcrumb.selectzones";

  public static final String BREADCRUMB_CREATEDISPLAY_KEY = "breadcrumb.createdisplay";
  public static final String BREADCRUMB_EDITDISPLAY_KEY = "breadcrumb.editdisplay";
  public static final String BREADCRUMB_SECTIONS_KEY = "breadcrumb.sections";
  public static final String BREADCRUMB_SUBSECTIONS_KEY = "breadcrumb.subsections";
  public static final String BREADCRUMB_ITEMS_KEY = "breadcrumb.items";
  public static final String BREADCRUMB_CREATEITEM_KEY = "breadcrumb.createitem";
  public static final String BREADCRUMB_EDITITEM_KEY = "breadcrumb.edititem";
  public static final String BREADCRUMB_ITEMPRICEUNIT_KEY = "breadcrumb.itempriceunit";
  public static final String BREADCRUMB_CREATEITEMPRICEUNIT_KEY = "breadcrumb.createitempriceunit";
  public static final String BREADCRUMB_CREATEITEMATTRIBUTE_KEY = "breadcrumb.createitemattribute";
  public static final String BREADCRUMB_ITEMATTRIBUTE_KEY = "breadcrumb.itemattribute";
  public static final String BREADCRUMB_CREATEPRINCIPAL_KEY = "breadcrumb.createprincipal";
  public static final String BREADCRUMB_EDITPRINCIPAL_KEY = "breadcrumb.editprincipal";
  public static final String BREADCRUMB_PRINCIPAL_KEY = "breadcrumb.principal";
  public static final String BREADCRUMB_SELECT_LOCATION_KEY = "breadcrumb.selectlocation";
  public static final String BREADCRUMB_SELECT_STORE_KEY = "breadcrumb.selectstore";
  public static final String BREADCRUMB_CREATE_ATTRIBUTE_KEY = "breadcrumb.createattribute";
  public static final String BREADCRUMB_EDIT_ATTRIBUTE_KEY = "breadcrumb.editattribute";
  public static final String BREADCRUMB_CREATE_PRICEUNIT_KEY = "breadcrumb.createpriceunit";
  public static final String BREADCRUMB_EDIT_PRICEUNIT_KEY = "breadcrumb.editpriceunit";
  public static final String BREADCRUMB_ATTRIBUTE_KEY = "breadcrumb.attribute";
  public static final String BREADCRUMB_PRICEUNIT_KEY = "breadcrumb.priceunit";
  public static final String BREADCRUMB_CREATEZONE_KEY = "breadcrumb.createzone";
  public static final String BREADCRUMB_ZONE_KEY = "breadcrumb.zone";
  public static final String BREADCRUMB_SECTIONLISTHOME_KEY = "breadcrumb.sectionlisthome";
  public static final String BREADCRUMB_CREATESECTION_KEY = "breadcrumb.createsection";
  public static final String BREADCRUMB_CREATESUBSECTION_KEY = "breadcrumb.createsubsection";
  public static final String BREADCRUMB_EDITSUBSECTION_KEY = "breadcrumb.editsubsection";
  public static final String BREADCRUMB_ZONES_KEY = "breadcrumb.zones";
  public static final String BREADCRUMB_EDITZONE_KEY = "breadcrumb.editzone";
  public static final String BREADCRUMB_EDITSECTION_KEY = "breadcrumb.editsection";
  public static final String BREADCRUMB_VIEWS_KEY = "breadcrumb.views";
  public static final String BREADCRUMB_CREATEVIEW_KEY = "breadcrumb.createview";
  public static final String BREADCRUMB_ITEMCATEGORY_KEY = "breadcrumb.itemcategory";
  public static final String BREADCRUMB_CREATEITEMCATEGORY_KEY = "breadcrumb.createcategory";
  public static final String BREADCRUMB_EDITITEMCATEGORY_KEY = "breadcrumb.editcategory";
  public static final String CLONE_SOURCE_INFORMATION_MESSAGE = "location.form.clone.information.message";

  // Special Characters
  public static final String COMMA = ",";
  public static final String COLON = ":";
  public static final String EMPTY = "";

  // Entity Fragments
  public static final String ENTITY_FRAGMENT_BRANDS = "brands";
  public static final String ENTITY_FRAGMENT_BRANDS_FILTER = "brand";
  public static final String ENTITY_FRAGMENT_LOCATIONS = "locations";
  public static final String ENTITY_FRAGMENT_LOCATIONS_FILTER = "location";
  public static final String ENTITY_FRAGMENT_STORES = "stores";
  public static final String ENTITY_FRAGMENT_STORES_FILTER = "store";
  public static final String ENTITY_FRAGMENT_DISPLAYS = "displays";
  public static final String ENTITY_FRAGMENT_DISPLAYS_FILTER = "display";
  public static final String ENTITY_FRAGMENT_SUBSECTIONS = "subsections";
  public static final String ENTITY_FRAGMENT_SUBSECTIONS_FILTER = "subsection";
  public static final String ENTITY_FRAGMENT_ZONES = "zones";
  public static final String ENTITY_FRAGMENT_ZONES_FILTER = "zone";
  public static final String ENTITY_FRAGMENT_ITEMS = "items";
  public static final String ENTITY_FRAGMENT_SECTIONS = "sections";
  public static final String ENTITY_FRAGMENT_SECTIONS_FILTER = "section";
  public static final String ENTITY_FRAGMENT_PRINCIPALS = "principals";
  public static final String ENTITY_FRAGMENT_PRINCIPALS_ADMIN = "principals/admin";
  public static final String ENTITY_FRAGMENT_PRINCIPALS_PASSWORD = "password";
  public static final String ENTITY_FRAGMENT_VIEWS = "groups";
  public static final String ENTITY_FRAGMENT_ITEMCATEGORY = "categories";
  public static final String ENTITY_FRAGMENT_ASSETS = "assets";

  // Password Reset
  public static final String PASSWORD_RESET_ERROR = "resetpassword.form.error";
  //Email Already Exist Constant
  public static final String USERNAME_ALREADY_EXIST = "username.already.exist.error";
  public static final String REQUIRED_FIELDS_MISSING = "required.fields.missing";
  public static final String REQUIRED_FIELDS_PASSWORD_INVALID = "required.fields.password.invalid";

  //Assets Exceptions
  public static final String REQUIRED_FIELDS_IMAGE_INVALID = "required.fields.images.invalid";
  public static final String REQUIRED_FIELDS_VIDEO_INVALID ="required.fields.video.invalid";

  // General Exceptions
  public static final String EXCEPTION_MESSAGE_NOACCESS = "exception.message.noaccess";
  public static final String EXCEPTION_RESOURCE_NOT_FOUND = "exception.message.resource.notfound";
  public static final String EXCEPTION_MESSAGE_UNKNOWN = "exception.message.unknown";

  // POS
  public static final String POS_LOCATION_TOKEN_KEY_TEMPLATE = "/dmcadmin/pos/{0}/{1}/{2}/{3}";
  public static final String POS_BRINK_ACCESS_TOKEN = "accessToken";
  public static final String POS_BRINK_LOCATION_TOKEN = "locationToken";
  public static final String POS_TOAST_CLIENT_ID = "clientId";
  public static final String POS_TOAST_CLIENT_SECRET = "clientSecret";
  public static final String POS_TOAST_RESTAURANT_GUID   = "restaurantGuid";
  public static final String POS_TOAST_HEADER_PARAM_AUTHORIZATION  = "Authorization";
  public static final String POS_TOAST_HEADER_PARAM_RESTAURANT_ID = "Toast-Restaurant-External-ID";
  public static final String POS_TOAST_INVENTORY_STATUS_OUT_OF_STOCK = "OUT_OF_STOCK";
  public static final String POS_TOAST_INVENTORY_STATUS_IN_STOCK = "IN_STOCK";
  public static final String POS_TOAST_INVENTORY_STATUS_QUANTITY = "QUANTITY";
  public static final String POS_TOAST_VISIBILITY_POS = "POS";

  public static final SimpleDateFormat POS_TOAST_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  public static final String POS_DMC_CONFIGURATION_KEY_TEMPLATE = "pos/{5}/brand/{0}/location/{1}/store/{2}/display/{3}/daypart/{4}";
  public static final String POS_DMC_CONFIGURATION_FILENAME_TEMPLATE = "{5}_brand_{0}_location_{1}_store_{2}_display_{3}_daypart_{4}.csv";
  public static final String[] POS_DMC_CONFIGURATION_MENU_NUMBER = {"POS Menu Id", ""};
  public static final String[] POS_DMC_CONFIGURATION_HEADERS = {"Type", "Item Id", "DMC Item Id", "DMC Price Attribute", "Mapped To Name","Mapped to Soldout", "DMC Item Name"};
  public static final String POS_CONFIGURATION_NOTIFICATION_SUBJECT = "pos.configuration.notification.email.subject";
  public static final String POS_DISABLED_ADMIN_NOTIFICATION = "pos.configuration.disabled";

  public static final String UPDATE_REASON_CODE_REFRESH = "REFRESH";
  public static final String UPDATE_REASON_CODE_RELOAD = "RELOAD";

}
