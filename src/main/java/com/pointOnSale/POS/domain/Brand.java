package com.pointOnSale.POS.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.URL;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
    value = {"createdAt", "updatedAt"},
    allowGetters = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "number"}))
@Data
@Log
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@SuppressFBWarnings(
    value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2", "USBR_UNNECESSARY_STORE_BEFORE_RETURN"},
    justification = "Lombok generated code")
public class Brand {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @EqualsAndHashCode.Include
  @ToString.Include
  private Long brandId;

  @Min(value = 0, message = "Please supply a positive integer >= 0 for Brand number")
  @ToString.Include
  private Integer number;

  @Embedded private Address address;

  @Size(max = 30)
  @Column(nullable = false)
  private String conceptName;

  @Size(max = 50)
  private String googlePlaceId;

  @Size(max = 30)
  @ToString.Include
  private String name;

  @Size(max = 30)
  private String owner;

  @Size(max = 30)
  private String phone;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Attribute> attributes = new ArrayList<>();

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderBy("name ASC")
  private List<PriceUnit> priceUnits = new ArrayList<>();

  @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @OrderBy("number ASC")
  private List<Location> locations = new ArrayList<Location>();

  @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Principal> principals = new ArrayList<Principal>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      joinColumns = @JoinColumn(name = "fk_brandId"),
      inverseJoinColumns = @JoinColumn(name = "fk_posSystemId"),name = "brand_pos_system")
  @Fetch(value = FetchMode.SUBSELECT)
  @JsonInclude
  private Set<PosSystem> posSystem = new HashSet<PosSystem>();

  private URL webSite;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedDate
  private Date createdAt;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @LastModifiedDate
  private Date updatedAt;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
  private List<DisplayGroups> groups;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
  private List<ItemCategory> itemCategories;

  @Column(name = "industry")
  private String industry;



  /**
   * Utility method to return stores counts
   *
   * @return
   */
  public Integer getStoreCount() {
    Integer stores = 0;
    for (Location location : locations) {
      stores += location.getStores().size();
    }
    return stores;
  }

  /**
   * Utility method to get stores
   *
   * @return
   */
  public List<Store> getStores() {
    List<Store> stores = new LinkedList<>();
    for (Location location : locations) {
      stores.addAll(location.getStores());
    }
    return stores;
  }

  /**
   * Utility method to return display counts
   *
   * @return
   */
  public Integer getDisplayCount() {
    Integer displayCount = 0;
    for (Location location : locations) {
      List<Store> stores = location.getStores();
      for (Store store : stores) {
        displayCount += store.getDisplays().size();
      }
    }
    return displayCount;
  }
}
