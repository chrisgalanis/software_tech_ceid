package com.example.roomie;

public class HouseListing {
  public final House house;
  public final String ownerName;
  public final String ownerAvatarUrl;
  public final String houseAvatarUrl;
  public final boolean isApproved; // new!

  public HouseListing(
      House house,
      String ownerName,
      String ownerAvatarUrl,
      String houseAvatarUrl,
      boolean isApproved) {
    this.house = house;
    this.ownerName = ownerName;
    this.ownerAvatarUrl = ownerAvatarUrl;
    this.houseAvatarUrl = houseAvatarUrl;
    this.isApproved = isApproved;
  }

  @Override
  public String toString() {
    return "HouseListing{"
        + "house="
        + house
        + ", ownerName='"
        + ownerName
        + '\''
        + ", ownerAvatarUrl='"
        + ownerAvatarUrl
        + '\''
        + ", houseAvatarUrl='"
        + houseAvatarUrl
        + '\''
        + '}';
  }
}
