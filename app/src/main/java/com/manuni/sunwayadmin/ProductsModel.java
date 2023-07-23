package com.manuni.sunwayadmin;

public class ProductsModel {
    private String perOrder, productId,productImage,productNumber,productSellingPrice;

    public ProductsModel() {
    }

    public ProductsModel(String perOrder, String productId, String productImage, String productNumber, String productSellingPrice) {
        this.perOrder = perOrder;
        this.productId = productId;
        this.productImage = productImage;
        this.productNumber = productNumber;
        this.productSellingPrice = productSellingPrice;
    }

    public String getPerOrder() {
        return perOrder;
    }

    public void setPerOrder(String perOrder) {
        this.perOrder = perOrder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(String productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }
}
