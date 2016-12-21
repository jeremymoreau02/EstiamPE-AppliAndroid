package com.jeremy.estiam.appliandroid.api;

public class AccessToken
{
  private String accessToken;
  private String tokenType;
  
  public String getAccessToken()
  {
    return this.accessToken;
  }
  
  public String getTokenType()
  {
    if (!Character.isUpperCase(this.tokenType.charAt(0))) {
      this.tokenType = (Character.toString(this.tokenType.charAt(0)).toUpperCase() + this.tokenType.substring(1));
    }
    return this.tokenType;
  }
}
