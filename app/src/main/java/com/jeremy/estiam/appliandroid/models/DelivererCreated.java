package com.jeremy.estiam.appliandroid.models;

public class DelivererCreated
{
  private int delivererID =0;
  private boolean success =true;

  public int getDelivererId() {
    return delivererID;
  }

  public void setDelivererId(int delivererId) {
    this.delivererID = delivererId;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
