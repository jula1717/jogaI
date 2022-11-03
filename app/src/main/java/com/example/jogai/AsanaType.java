package com.example.jogai;

public class AsanaType {
    String type;
    int id;

    public static final int POZYCJA_STOJACA=1;
    public static final int POZYCJA_SIEDZACA=2;
    public static final int SKLON_DO_PRZODU=3;
    public static final int WYGIECIE_DO_TYLU=4;
    public static final int SKRET_TULOWIA=5;
    public static final int POZYCJA_ODWROCONA=6;
    public static final int POZYCJA_RELAKSACYJNA=7;


    public AsanaType(String type) {
        this.type = type;
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
}
