package com.example.jogai.comparators;

import com.example.jogai.Asana;
import com.example.jogai.JogaContract;

import java.util.Comparator;

public class SanskritComparator implements Comparator<Asana> {
    @Override
    public int compare(Asana a1, Asana a2) {
        return a1.getSanskritName().compareTo(a2.getSanskritName());
    }
}

