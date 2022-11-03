package com.example.jogai;

public class Asana {
    int id;
    String sanskritName;
    String name;
    byte[] image;
    String description;
    int columnTypeId;
    byte difficulty;
    boolean done;

    public Asana(String sanskritName, String name, byte[] image, String description, int columnTypeId, byte difficulty, boolean done) {
        this.sanskritName = sanskritName;
        this.name = name;
        this.image = image;
        this.description = description;
        this.columnTypeId = columnTypeId;
        this.difficulty = difficulty;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSanskritName() {
        return sanskritName;
    }

    public void setSanskritName(String sanskritName) {
        this.sanskritName = sanskritName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColumnTypeId() {
        return columnTypeId;
    }

    public void setColumnTypeId(int columnTypeId) {
        this.columnTypeId = columnTypeId;
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
