package com.jeremy.estiam.appliandroid.models;

import android.net.Uri;

import java.util.concurrent.atomic.AtomicInteger;

public class PhotoModifiee
{
  private int id;
  private int idUser;
  private String name;
  private String uriOrigine;
  private String uriFinale;
  private int idFormat;
  private int idMasque;
  private String description;
  private int nbPhotos = 1;
  private float prix;
  private int idPanier;

  public PhotoModifiee( )
  {
    this.id=0;
  }

  public PhotoModifiee(String name, String uriOrigine)
  {
    this.id=0;
    this.nbPhotos=1;
    this.name = name;
    this.uriOrigine = uriOrigine;
  }

  public int getIdUser() {
    return idUser;
  }

  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  public int getIdPanier() {
    return idPanier;
  }

  public void setIdPanier(int idPanier) {
    this.idPanier = idPanier;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getNbPhotos() {
    return nbPhotos;
  }

  public void setNbPhotos(int nbPhotos) {
    this.nbPhotos = nbPhotos;
  }

  public float getPrix() {
    return prix;
  }

  public void setPrix(float prix) {
    this.prix = prix;
  }

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

  public String getUriOrigine() {
    return uriOrigine;
  }

  public void setUriOrigine(String uriOrigine) {
    this.uriOrigine = uriOrigine;
  }

  public String getUriFinale() {
    return uriFinale;
  }

  public void setUriFinale(String uriFinale) {
    this.uriFinale = uriFinale;
  }

  public int getIdFormat() {
    return idFormat;
  }

  public void setIdFormat(int idFormat) {
    this.idFormat = idFormat;
  }

  public int getIdMasque() {
    return idMasque;
  }

  public void setIdMasque(int idMasque) {
    this.idMasque = idMasque;
  }
}
