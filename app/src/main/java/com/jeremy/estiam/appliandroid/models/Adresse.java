package com.jeremy.estiam.appliandroid.models;

public class Adresse
{
  private String cp;
  private String rue;
  private String ville;
  private int idUser;
  
  public String getCp()
  {
    return this.cp;
  }
  
  public void setCp(String cp)
  {
    this.cp = cp;
  }
  
  public String getRue()
  {
    return this.rue;
  }
  
  public void setRue(String rue)
  {
    this.rue = rue;
  }
  
  public String getVille()
  {
    return this.ville;
  }
  
  public void setVille(String ville)
  {
    this.ville = ville;
  }
  
  public int getIdUser()
  {
    return this.idUser;
  }
  
  public void setIdUser(int idUser)
  {
    this.idUser = idUser;
  }
}
