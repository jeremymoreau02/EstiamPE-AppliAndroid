package com.jeremy.estiam.appliandroid.models;

public class Shipping
{
  private String name;
  private float price;
  private int shippingDuration;

  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public int getShippingDuration() {
    return shippingDuration;
  }

  public void setShippingDuration(int shippingDuration) {
    this.shippingDuration = shippingDuration;
  }
}
