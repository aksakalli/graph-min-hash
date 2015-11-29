package com.github.aksakalli.util;

import java.util.Set;
import java.util.TreeSet;

/**
 * Jaccard Similarity of sets:
 * <p>
 * <pre>
 *  D(S1,S2) = |S1 ^ S2|
 *             ---------
 *             |S1 u S2|
 *  </pre>
 */
public class Jaccard {

    public static <T> Double calculateSimilarity(Set<T> s1, Set<T> s2) {

        //If S1 and S2 are both empty, we define J(S1,S2) = 1
        if (s1.isEmpty() && s2.isEmpty()) {
            return 1.0;
        }

        //Union
        Set<T> union = new TreeSet<>();
        union.addAll(s1);
        union.addAll(s2);

        //Intersection
        Set<T> intersection = new TreeSet<>();
        intersection.addAll(s1);
        intersection.retainAll(s2);

        return (double) intersection.size() / (double) union.size();
    }
}
