package com.jeremy.estiam.appliandroid.models;

import android.net.Uri;

import java.util.concurrent.atomic.AtomicInteger;

public class Destinataires
{
  private static final AtomicInteger count = new AtomicInteger(1);
  private int id;
  private int idPhoto;
  private String civilite;
  private String nom;
  private String prenom;
  private String mobile;
  private String email;
  private String rue;
  private String cp;
  private String ville;

  public Destinataires( String civilite, String nom, String prenom, String mobile, String email, String rue, String cp, String ville) {
    this.id = count.incrementAndGet();
    this.civilite = civilite;
    this.nom = nom;
    this.prenom = prenom;
    this.mobile = mobile;
    this.email = email;
    this.rue = rue;
    this.cp = cp;
    this.ville = ville;
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
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
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
    return rue;
  }

  public void setRue(String rue) {
    this.rue = rue;
  }

  public String getCp() {
    return cp;
  }

  public void setCp(String cp) {
    this.cp = cp;
  }

  public String getVille() {
    return ville;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }
}
