package com.jeremy.estiam.appliandroid.models;

import java.util.List;

public class PhotoModifiee
{
  private int photoId;
  private int idUser;
  private String name;
  private String uriOrigine;
  private String uriFinale;
  private int idFormat;
  private int maskId;
  private String description;
  private int nbPhotos = 1;
  private float prix;
  private int idPanier;
  private List<Destinataires> deliverers;

  public List<Destinataires> getDeliverers() {
    return deliverers;
  }

  public void setDeliverers(List<Destinataires> deliverers) {
    this.deliverers = deliverers;
  }

  public PhotoModifiee( )
  {
    this.photoId =0;
  }

  public PhotoModifiee(String name, String uriOrigine)
  {
    this.photoId =0;
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

  public int getPhotoId() {
    return photoId;
  }

  public void setPhotoId(int id) {
    this.photoId = id;
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

  public int getMaskId() {
    return maskId;
  }

  public void setMaskId(int maskId) {
    this.maskId = maskId;
  }
}
