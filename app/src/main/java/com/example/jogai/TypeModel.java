package com.example.jogai;

public class TypeModel {
    String type;
    int imgRes;
    int id;

    public TypeModel() {
    }

    public TypeModel(String type, int imgRes) {
        this.type = type;
        this.imgRes = imgRes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}
