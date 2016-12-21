package com.jeremy.estiam.appliandroid.models;

import android.net.Uri;

public class Photo
{
  private int id;
  private String name;
  private String path;
  private Uri uri;
  
  public Photo(int id, String name, Uri uri)
  {
    this.id = id;
    this.name = name;
    this.uri = uri;
  }
  
  public Uri getUri()
  {
    return this.uri;
  }
  
  public void setUri(Uri uri)
  {
    this.uri = uri;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public void setPath(String path)
  {
    this.path = path;
  }
}
