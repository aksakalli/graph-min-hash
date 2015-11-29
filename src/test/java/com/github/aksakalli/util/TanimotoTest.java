package com.github.aksakalli.util;

import static com.github.aksakalli.util.Tanimoto.calculateSimilarity;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TanimotoTest {

    private static final double DELTA = 1e-15;

    List<Integer> sketch1 = Arrays.asList(1, 2, 3);
    List<Integer> sketch2 = Arrays.asList(1, 3, 2, 4);
    List<Integer> sketch3 = Arrays.asList(1, 2, 3, 4);
    List<Integer> sketch3Copy = Arrays.asList(1, 2, 3, 4);
    List<Integer> sketch4 = Arrays.asList(1, 3, 4, 5);

    @Test(expected = IllegalArgumentException.class)
    public final void sketchSizesShouldBeTheSame() {
        calculateSimilarity(sketch1, sketch2);
    }

    @Test
    public final void shouldComputeDistance() {
        assertEquals(0.5, calculateSimilarity(sketch2, sketch3), DELTA);
        assertEquals(0.25, calculateSimilarity(sketch3, sketch4), DELTA);
    }

    @Test
    public final void shouldBeOneForTheSameSketch() {
        assertEquals(1, calculateSimilarity(sketch3, sketch3Copy), DELTA);
    }

}
