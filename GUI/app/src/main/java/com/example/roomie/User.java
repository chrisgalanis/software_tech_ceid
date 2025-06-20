package com.example.roomie;

import androidx.annotation.Nullable;
import java.util.Calendar;

public class User {
  public long id;
  public String firstName;
  public String lastName;
  public String gender; // e.g. "male", "female", "other"
  public String birthday; // ISO format "yyyy-MM-dd"
  public boolean hasHouse;
  public String city;
  public int minBudget;
  public int maxBudget;
  public String avatarUrl; // URL to user's avatar image

  public User() {}

  public User(
      long id,
      String firstName,
      String lastName,
      String gender,
      String birthday,
      boolean hasHouse,
      String city,
      int minBudget,
      int maxBudget,
      String avatarUrl) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthday = birthday;
    this.hasHouse = hasHouse;
    this.city = city;
    this.minBudget = minBudget;
    this.maxBudget = maxBudget;
    this.avatarUrl = avatarUrl;
  }

  public String getDisplayName() {
    String name = "";
    if (firstName != null && !firstName.isEmpty()) {
      name += firstName;
    }
    if (lastName != null && !lastName.isEmpty()) {
      if (!name.isEmpty()) name += " ";
      name += lastName;
    }
    return name.isEmpty() ? "User" : name; // Fallback if no names
  }


  public int getAge() {
    return calculateAge(birthday);
  }

  /**
   * Helper to compute age from a birthday string in ISO format.
   *
   * @param birthday ISO date string "yyyy-MM-dd"
   * @return years between birthday and today, or -1 on error
   */
  public static int calculateAge(@Nullable String birthday) {
    if (birthday == null) return -1;
    String[] parts = birthday.split("/");
    if (parts.length < 3) return -1;

    int year, month, day;
    try {
      year = Integer.parseInt(parts[0]);
      month = Integer.parseInt(parts[1]);
      day = Integer.parseInt(parts[2]);
    } catch (NumberFormatException e) {
      return -1;
    }

    // birth date
    Calendar dob = Calendar.getInstance();
    dob.set(year, month - 1, day);

    // today
    Calendar today = Calendar.getInstance();

    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

    // if today's date is before birthday this year, subtract one
    int todayMonth = today.get(Calendar.MONTH);
    int dobMonth = dob.get(Calendar.MONTH);
    int todayDay = today.get(Calendar.DAY_OF_MONTH);
    int dobDay = dob.get(Calendar.DAY_OF_MONTH);

    if (todayMonth < dobMonth || (todayMonth == dobMonth && todayDay < dobDay)) {
      age--;
    }

    return age;
  }

  public String pronounsForGender(String g) {
    if (g == null) return null;
    g = g.toLowerCase();
    switch (g) {
      case "male":
        return "he/him";
      case "female":
        return "she/her";
      default:
        return "they/them";
    }
  }
}
