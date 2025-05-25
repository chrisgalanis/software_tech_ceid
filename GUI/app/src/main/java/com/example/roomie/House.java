package com.example.roomie;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class House {
  public final long id;
  public final String address;
  public final double rent;
  public final double area;
  public final int floor;
  public final List<String> photoUrls;
  // new fields:
  public final String ownerName;
  public final String ownerAvatarUrl;
  public final LatLng location;

  public House(
      long id,
      String address,
      double rent,
      double area,
      int floor,
      List<String> photoUrls,
      String ownerName,
      String ownerAvatarUrl,
      LatLng location) {
    this.id = id;
    this.address = address;
    this.rent = rent;
    this.area = area;
    this.floor = floor;
    this.photoUrls = photoUrls;
    this.ownerName = ownerName;
    this.ownerAvatarUrl = ownerAvatarUrl;
    this.location = location;
  }
}
