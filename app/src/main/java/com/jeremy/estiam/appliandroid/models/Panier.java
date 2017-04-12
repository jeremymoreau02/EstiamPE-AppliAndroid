package com.jeremy.estiam.appliandroid.models;

import java.util.List;

public class Panier
{
  private int id;
  private int shippingMethodId;
  private int userID;
  private int nbPhotos;
  private float totalPriceHT;
  private float prixTTC;
  private float fdp;
  private float prixTotal;
  private String nomFacturation;
  private String prenomFacturation;
  private String cpFacturation;
  private String villeFacturation;
  private String rueFacturation;
  private int billingAddressId;
  private String status;
  private List<PhotoModifiee> Items;

  public List<PhotoModifiee> getItems() {
    return Items;
  }

  public void setItems(List<PhotoModifiee> items) {
    Items = items;
  }

  public int getUserId() {
    return userID;
  }

  public void setUserId(int userId) {
    this.userID = userId;
  }

  public int getShippingMethodId() {
    return shippingMethodId;
  }

  public void setShippingMethodId(int shippingMethodId) {
    this.shippingMethodId = shippingMethodId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getBillingAdressId() {
    return billingAddressId;
  }

  public void setBillingAdressId(int billingAdressId) {
    this.billingAddressId = billingAdressId;
  }

  public Panier() {
    nbPhotos=1;
    status = "Pending";
  }

  public String getNomFacturation() {
    return nomFacturation;
  }

  public void setNomFacturation(String nomFacturation) {
    this.nomFacturation = nomFacturation;
  }

  public String getPrenomFacturation() {
    return prenomFacturation;
  }

  public void setPrenomFacturation(String prenomFacturation) {
    this.prenomFacturation = prenomFacturation;
  }

  public String getCpFacturation() {
    return cpFacturation;
  }

  public void setCpFacturation(String cpFacturation) {
    this.cpFacturation = cpFacturation;
  }

  public String getVilleFacturation() {
    return villeFacturation;
  }

  public void setVilleFacturation(String villeFacturation) {
    this.villeFacturation = villeFacturation;
  }

  public String getRueFacturation() {
    return rueFacturation;
  }

  public void setRueFacturation(String rueFacturation) {
    this.rueFacturation = rueFacturation;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getNbPhotos() {
    return nbPhotos;
  }

  public void setNbPhotos(int nbPhotos) {
    this.nbPhotos = nbPhotos;
  }

  public float getTotalPriceHT() {
    return totalPriceHT;
  }

  public void setTotalPriceHT(float totalPriceHT) {
    this.totalPriceHT = totalPriceHT;
  }

  public float getPrixTTC() {
    return prixTTC;
  }

  public void setPrixTTC(float prixTTC) {
    this.prixTTC = prixTTC;
  }

  public float getFdp() {
    return fdp;
  }

  public void setFdp(float fdp) {
    this.fdp = fdp;
  }

  public float getPrixTotal() {
    return prixTotal;
  }

  public void setPrixTotal(float prixTotal) {
    this.prixTotal = prixTotal;
  }
}
