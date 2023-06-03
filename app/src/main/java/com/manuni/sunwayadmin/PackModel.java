package com.manuni.sunwayadmin;

public class PackModel {
    private String id,levelName,perOrderQuantity,packImage,sellingPrice;

    public PackModel() {
    }

    public PackModel(String id, String levelName, String perOrderQuantity,String packImage) {
        this.id = id;
        this.levelName = levelName;
        this.perOrderQuantity = perOrderQuantity;
        this.packImage = packImage;

    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getPackImage() {
        return packImage;
    }

    public void setPackImage(String packImage) {
        this.packImage = packImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getPerOrderQuantity() {
        return perOrderQuantity;
    }

    public void setPerOrderQuantity(String perOrderQuantity) {
        this.perOrderQuantity = perOrderQuantity;
    }
}
