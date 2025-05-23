package com.example.roomie;

public class User {
    public long    id;
    public String  firstName;
    public String  lastName;
    public int     age;        // or calculate from birthday if you like
    public boolean hasHouse;   // set this when you insert users
    public int     budget;
    public String  city;

    public User() { }

    public User(long id, String firstName, String lastName,
                int age, boolean hasHouse, int budget, String city) {
        this.id         = id;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.age        = age;
        this.hasHouse   = hasHouse;
        this.budget     = budget;
        this.city       = city;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName +
                " (age " + age + ")  house? " + hasHouse +
                "  budget:€" + budget +
                "  city:" + city;
    }
}
