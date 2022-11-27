package com.example.jogai.comparators;

import com.example.jogai.AsanaModel;

import java.util.Comparator;

public class NameComparator implements Comparator<AsanaModel> {
    @Override
    public int compare(AsanaModel a1, AsanaModel a2) {
        return a1.getName().compareTo(a2.getName());
    }
}
