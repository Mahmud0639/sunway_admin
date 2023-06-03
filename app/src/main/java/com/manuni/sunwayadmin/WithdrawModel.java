package com.manuni.sunwayadmin;

public class WithdrawModel {
    private String complete,count,image,name,remainBalance,status,timestamp,trcAddress,uid,withdraw;

    public WithdrawModel() {
    }

    public WithdrawModel(String complete, String count, String image, String name, String remainBalance, String status, String timestamp, String trcAddress, String uid, String withdraw) {
        this.complete = complete;
        this.count = count;
        this.image = image;
        this.name = name;
        this.remainBalance = remainBalance;
        this.status = status;
        this.timestamp = timestamp;
        this.trcAddress = trcAddress;
        this.uid = uid;
        this.withdraw = withdraw;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemainBalance() {
        return remainBalance;
    }

    public void setRemainBalance(String remainBalance) {
        this.remainBalance = remainBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTrcAddress() {
        return trcAddress;
    }

    public void setTrcAddress(String trcAddress) {
        this.trcAddress = trcAddress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }
}
