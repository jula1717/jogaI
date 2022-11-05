package com.example.jogai;

import android.os.Parcel;
import android.os.Parcelable;

public class Asana implements Parcelable {
    int id;
    String sanskritName;
    String name;
    byte[] image;
    String description;
    int columnTypeId;
    byte difficulty;
    boolean done;

    public Asana() {
    }

    public Asana(String sanskritName, String name, byte[] image, String description, int columnTypeId, byte difficulty, boolean done) {
        this.sanskritName = sanskritName;
        this.name = name;
        this.image = image;
        this.description = description;
        this.columnTypeId = columnTypeId;
        this.difficulty = difficulty;
        this.done = done;
    }

    protected Asana(Parcel in) {
        id = in.readInt();
        sanskritName = in.readString();
        name = in.readString();
        image = in.createByteArray();
        description = in.readString();
        columnTypeId = in.readInt();
        difficulty = in.readByte();
        done = in.readByte() != 0;
    }

    public static final Creator<Asana> CREATOR = new Creator<Asana>() {
        @Override
        public Asana createFromParcel(Parcel in) {
            return new Asana(in);
        }

        @Override
        public Asana[] newArray(int size) {
            return new Asana[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(sanskritName);
        parcel.writeString(name);
        parcel.writeByteArray(image);
        parcel.writeString(description);
        parcel.writeInt(columnTypeId);
        parcel.writeByte(difficulty);
        parcel.writeByte((byte) (done ? 1 : 0));
    }
}
