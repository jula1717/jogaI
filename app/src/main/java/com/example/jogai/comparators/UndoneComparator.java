package com.example.jogai.comparators;

import com.example.jogai.Asana;

import java.util.Comparator;

public class UndoneComparator implements Comparator<Asana> {
    @Override
    public int compare(Asana a1, Asana a2) {
        return Boolean.compare(a1.isDone(), a2.isDone());
    }
}
