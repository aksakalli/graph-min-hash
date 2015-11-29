package com.github.aksakalli.util;

import org.junit.Test;

import static com.github.aksakalli.util.Jaccard.calculateSimilarity;
import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JaccardTest {

    private static final double DELTA = 1e-15;

    Set<String> emptySet = new HashSet<>();
    Set<String> emptySet2 = new HashSet<>();
    Set<String> s1 = new HashSet<>(Arrays.asList("A", "B", "C"));
    Set<String> s2 = new HashSet<>(Arrays.asList("B", "C"));
    Set<String> s3 = new HashSet<>(Arrays.asList("C"));
    Set<String> s4 = new HashSet<>(Arrays.asList("B", "C", "D"));
    Set<String> s5 = new HashSet<>(Arrays.asList("A", "D"));


    @Test
    public final void twoEmptySetsShouldBeOne() {
        assertEquals(1.0, calculateSimilarity(emptySet, emptySet2), DELTA);
    }

    @Test
    public final void emptySetShouldBeZeroComparedToNonempty() {
        assertEquals(0.0, calculateSimilarity(emptySet, s1), DELTA);
    }

    @Test
    public final void someConcreteExamples() {
        assertEquals(2.0 / 3.0, calculateSimilarity(s1, s2), DELTA);
        assertEquals(0.5, calculateSimilarity(s2, s3), DELTA);
        assertEquals(0.5, calculateSimilarity(s1, s4), DELTA);
        //Order should not matter
        assertEquals(0.5, calculateSimilarity(s4, s1), DELTA);
        //completely different sets
        assertEquals(0.0, calculateSimilarity(s2, s5), DELTA);
    }

}
