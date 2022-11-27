package com.example.jogai.comparators;

import com.example.jogai.AsanaModel;

import java.util.Comparator;

public class TypeComparator implements Comparator<AsanaModel> {
    @Override
    public int compare(AsanaModel a1, AsanaModel a2) {
        return Integer.compare(a1.getColumnTypeId(), a2.getColumnTypeId());
    }
}
