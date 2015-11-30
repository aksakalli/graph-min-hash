package com.github.aksakalli.util;

import java.util.List;
import java.util.stream.IntStream;

public class Tanimoto {

    public static <T> Double calculateSimilarity(List<T> sketch1, List<T> sketch2)
            throws IllegalArgumentException {
        if (sketch1.size() != sketch2.size()) {
            throw new IllegalArgumentException("Sketch sizes should be the same.");
        }

        return IntStream.range(0, sketch1.size())
                .map(i -> sketch1.get(i).equals(sketch2.get(i)) ? 1 : 0)
                .average()
                .getAsDouble();

    }

    public static <T> Double calculateSimilarity(T[] sketch1, T[] sketch2)
            throws IllegalArgumentException {
        if (sketch1.length != sketch2.length) {
            throw new IllegalArgumentException("Sketch sizes should be the same.");
        }

        int theSameElementCount = 0;

        for (int i = 0; i < sketch1.length; i++) {
            if(sketch1[i].equals(sketch2[i])){
                theSameElementCount++;
            }
        }
        return (double)theSameElementCount/(double)sketch1.length;

    }
}
