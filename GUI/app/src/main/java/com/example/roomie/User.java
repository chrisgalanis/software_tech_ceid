package com.example.roomie;

public class User {
  public long id;
  public String firstName;
  public String lastName;
  public int age; // or calculate from birthday if you like
  public boolean hasHouse; // set this when you insert users
  public String city;

  // New fields for range filtering:
  public int minBudget;
  public int maxBudget;

  public User() {}

  public User(
      long id,
      String firstName,
      String lastName,
      int age,
      boolean hasHouse,
      int budget,
      String city,
      int minBudget,
      int maxBudget) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.hasHouse = hasHouse;
    this.city = city;
    this.minBudget = minBudget;
    this.maxBudget = maxBudget;
  }

  @Override
  public String toString() {
    return firstName
        + " "
        + lastName
        + " (age "
        + age
        + ")"
        + " house? "
        + hasHouse
        + " city: "
        + city
        + " [min:€"
        + minBudget
        + " max:€"
        + maxBudget
        + "]";
  }
}
