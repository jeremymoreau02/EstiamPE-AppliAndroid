package com.jeremy.estiam.appliandroid.models;

public class Panier
{
  private int id;
  private int idLivraison;
  private int idUser;
  private int nbPhotos;
  private float prixHT;
  private float prixTTC;
  private float fdp;
  private float prixTotal;
  private String nomFacturation;
  private String prenomFacturation;
  private String cpFacturation;
  private String villeFacturation;
  private String rueFacturation;
  private int AddressId;
  private String status;

  public int getIdUser() {
    return idUser;
  }

  public void setIdUser(int idUser) {
    this.idUser = idUser;
  }

  public int getIdLivraison() {
    return idLivraison;
  }

  public void setIdLivraison(int idLivraison) {
    this.idLivraison = idLivraison;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getAddressId() {
    return AddressId;
  }

  public void setAddressId(int addressId) {
    AddressId = addressId;
  }

  public Panier() {
    nbPhotos=1;
    status = "Preparing";
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

  public float getPrixHT() {
    return prixHT;
  }

  public void setPrixHT(float prixHT) {
    this.prixHT = prixHT;
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
