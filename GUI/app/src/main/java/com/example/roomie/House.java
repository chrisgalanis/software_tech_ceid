package com.example.roomie;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;


public class House {
  public long id;
  public long ownerId;
  public String address;
  public double rent;
  public double area;
  public int floor;
  public double latitude;
  public double longitude;
  public final List<String> photoUrls;

  public House(long id,
               long ownerId,
               String address,
               double rent,
               double area,
               int floor,
               double latitude,
               double longitude,
               List<String> photoUrls) {
    this.id = id;
    this.ownerId = ownerId;
    this.address = address;
    this.rent = rent;
    this.area = area;
    this.floor = floor;
    this.latitude = latitude;
    this.longitude = longitude;
    this.photoUrls = photoUrls;
  }

  public LatLng getLocation() {
    return new LatLng(latitude, longitude);
  }
}
