package com.dv.genaitraining.features.community;

/**
 * Optional location details for a community.
 *
 * @param country optional country
 * @param region optional region
 * @param city optional city
 */
public record CommunityLocation(
    CommunityCountry country,
    String region,
    String city
) {
  /**
   * Indicates whether this location has at least one non-empty value.
   *
   * @return true if location contains any data
   */
  public boolean hasAnyValue() {
    return country != null
        || (region != null && !region.isBlank())
        || (city != null && !city.isBlank());
  }
}
