package com.jeremy.estiam.appliandroid.models;

public class PhotoCreated
{
  private int photoId =0;
  private boolean success =true;
  private String message="";
  private String mask="";

  public int getPhotoId() {
    return photoId;
  }

  public void setPhotoId(int photoId) {
    this.photoId = photoId;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMask() {
    return mask;
  }

  public void setMask(String mask) {
    this.mask = mask;
  }
}
