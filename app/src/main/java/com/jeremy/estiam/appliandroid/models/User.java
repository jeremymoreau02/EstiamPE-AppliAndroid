package com.jeremy.estiam.appliandroid.models;

public class User
{
  private String nom;
  private String prenom;
  private String birthday;
  private String email;
  private String pseudo;
  private String password;
  private String oldPassword;
  private String message;
  private int id;
  private String addresseId;
  private String token;
  private String role;
  private String age;
  
  public String getOldPassword()
  {
    return this.oldPassword;
  }
  
  public void setOldPassword(String oldPassword)
  {
    this.oldPassword = oldPassword;
  }
  
  public String getAddresseId()
  {
    return this.addresseId;
  }
  
  public void setAddresseId(String adresseId)
  {
    this.addresseId = adresseId;
  }
  
  public String getRole()
  {
    return this.role;
  }
  
  public void setRole(String role)
  {
    this.role = role;
  }
  
  public String getAge()
  {
    return this.age;
  }
  
  public void setAge(String age)
  {
    this.age = age;
  }
  
  public String getToken()
  {
    return this.token;
  }
  
  public void setToken(String token)
  {
    this.token = token;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public String getMessage()
  {
    return this.message;
  }
  
  public void setMessage(String message)
  {
    this.message = message;
  }
  
  public String getNom()
  {
    return this.nom;
  }
  
  public void setNom(String nom)
  {
    this.nom = nom;
  }
  
  public String getPrenom()
  {
    return this.prenom;
  }
  
  public void setPrenom(String prenom)
  {
    this.prenom = prenom;
  }
  
  public String getBirthday()
  {
    return this.birthday;
  }
  
  public void setBirthday(String birthday)
  {
    this.birthday = birthday;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public String getPseudo()
  {
    return this.pseudo;
  }
  
  public void setPseudo(String pseudo)
  {
    this.pseudo = pseudo;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
}
