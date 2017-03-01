package com.jeremy.estiam.appliandroid.models;

public class Adresse
{
  private String ZC;
  private String street;
  private String city;
  private String type;
  private int UserId;
  private int id;
  private String createdAt;
  private String updatedAt;

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getZC() {
    return ZC;
  }

  public void setZC(String ZC) {
    this.ZC = ZC;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getUserId() {
    return UserId;
  }

  public void setUserId(int userId) {
    UserId = userId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
