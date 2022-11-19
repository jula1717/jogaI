package com.example.jogai.comparators;

import com.example.jogai.Asana;

import java.util.Comparator;

public class NameComparator implements Comparator<Asana> {
    @Override
    public int compare(Asana a1, Asana a2) {
        return a1.getName().compareTo(a2.getName());
    }
}
