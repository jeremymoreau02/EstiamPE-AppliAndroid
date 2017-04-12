package com.jeremy.estiam.appliandroid.models;

import android.net.Uri;

import java.util.concurrent.atomic.AtomicInteger;

public class Destinataires
{
  private int id;
  private int idPhoto;
  private int idMessage;
  private int UserId;
  private String civilite;
  private String name;
  private String prenom;
  private String mobile;
  private String email;
  private String street;
  private String ZC;
  private String city;
  private boolean isSelected;
  private String message = "";

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getIdUser() {
    return UserId;
  }

  public void setIdUser(int idUser) {
    this.UserId = idUser;
  }

  public Destinataires(int id, int idUser, String civilite, String nom, String prenom, String mobile, String email, String rue, String cp, String ville) {
    this.id = id;
    this.UserId = idUser;
    this.civilite = civilite;
    this.name = nom;
    this.prenom = prenom;
    this.mobile = mobile;
    this.email = email;
    this.street = rue;
    this.ZC = cp;
    this.city = ville;
    this.isSelected = true;
  }

  public int getIdMessage() {
    return idMessage;
  }

  public void setIdMessage(int idMessage) {
    this.idMessage = idMessage;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean selected) {
    isSelected = selected;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getIdPhoto() {
    return idPhoto;
  }

  public void setIdPhoto(int idPhoto) {
    this.idPhoto = idPhoto;
  }

  public String getCivilite() {
    return civilite;
  }

  public void setCivilite(String civilite) {
    this.civilite = civilite;
  }

  public String getNom() {
    return name;
  }

  public void setNom(String nom) {
    this.name = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRue() {
    return street;
  }

  public void setRue(String rue) {
    this.street = rue;
  }

  public String getCp() {
    return ZC;
  }

  public void setCp(String cp) {
    this.ZC = cp;
  }

  public String getVille() {
    return city;
  }

  public void setVille(String ville) {
    this.city = ville;
  }
}
