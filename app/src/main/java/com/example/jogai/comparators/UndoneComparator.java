package com.example.jogai.comparators;

import com.example.jogai.AsanaModel;

import java.util.Comparator;

public class UndoneComparator implements Comparator<AsanaModel> {
    @Override
    public int compare(AsanaModel a1, AsanaModel a2) {
        return Boolean.compare(a1.isDone(), a2.isDone());
    }
}
