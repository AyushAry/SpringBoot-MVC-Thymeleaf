package com.pointOnSale.POS.domain.support;

/**
 * Defines the roles available in system
 */
public class RoleTypeEnum {
  public enum Role {
    SUPER_ADMIN("Super Admin"),
    BRAND_ADMIN("Brand Admin"),
    LOCATION_ADMIN("Location Admin"),
    STORE_ADMIN("Store Admin"),
    ANNONYMOUS("ANNONYMOUS");

    private String displayName;

    Role(String displayName) {
      this.displayName = displayName;
    }

    public String getDisplayName() {
      return displayName;
    }

  }
}
