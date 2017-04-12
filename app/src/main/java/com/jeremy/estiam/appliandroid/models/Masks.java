package com.jeremy.estiam.appliandroid.models;

/**
 * Created by jeremy on 30/03/2017.
 */

public class Masks {
    private int id;
    private String name = "";
    private float price = 0;
    private String filePath= "";
    private String createdAt = "";
    private String updatedAt = "";
    private int DimensionId = 0;
    private Dimensions Dimension = new Dimensions();

    public Dimensions getDimension() {
        return Dimension;
    }

    public void setDimension(Dimensions Dimension) {
        this.Dimension = Dimension;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDimensionId() {
        return DimensionId;
    }

    public void setDimensionId(int dimensionId) {
        DimensionId = dimensionId;
    }
}
