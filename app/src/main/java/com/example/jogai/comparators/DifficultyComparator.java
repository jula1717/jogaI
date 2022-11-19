package com.example.jogai.comparators;

import com.example.jogai.Asana;

import java.util.Comparator;

public class DifficultyComparator implements Comparator<Asana> {
    @Override
    public int compare(Asana a1, Asana a2) {
        return Integer.compare(a1.getDifficulty(), a2.getDifficulty());
    }
}
