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
}
