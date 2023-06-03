package com.manuni.sunwayadmin;

public class PackageModelAdmin {
    private String packId,packKey,packName,userId,price,accountNumber;

    public PackageModelAdmin() {
    }

    public PackageModelAdmin(String packId, String packKey, String packName, String userId,String price) {
        this.packId = packId;
        this.packKey = packKey;
        this.packName = packName;
        this.userId = userId;
        this.price = price;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackKey() {
        return packKey;
    }

    public void setPackKey(String packKey) {
        this.packKey = packKey;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
