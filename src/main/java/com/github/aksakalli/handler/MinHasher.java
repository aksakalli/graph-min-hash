package com.github.aksakalli.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates group of min hash functions, computes the sketch.
 * <p>
 * NOTE: Sketches valid for the same function set, use the same instance!
 * </p>
 */
public class MinHasher {

    final private static Logger logger = LoggerFactory.getLogger(MinHasher.class);

    private static final int DEFAULT_SKETCH_SIZE = 16;
    private int sketchSize;
    private List<Integer> hashFunctions;

    public MinHasher(int sketchSize) {
        this.sketchSize = sketchSize;
        initilizeRandomHashFunctions();
    }

    public MinHasher() {
        this(DEFAULT_SKETCH_SIZE);
    }

    public int getSketchSize() {
        return sketchSize;
    }

    private void initilizeRandomHashFunctions() {
        Random random = new Random();
        hashFunctions = Arrays.stream(new Integer[this.sketchSize])
                .map(i -> i = random.nextInt())
                .collect(Collectors.toList());

        //First hash permutation is the default order
        hashFunctions.set(0, 0);
        logger.info("MinHasher was initialized with sketchSize = {}", this.sketchSize);
    }


    /**
     * computes the sketch based on minimum value of random permutation of fingerprint list
     *
     * @param fingerprintSet set of fingerprints extracted from molecule
     * @return sketch
     */
    public List<Integer> getSketchFromFingerprintSet(Set<Integer> fingerprintSet) {
        //instead of the fingerprint list, computes the XOR of random hash function factor
        return hashFunctions.stream()
                .map(h -> fingerprintSet.stream().min(Comparator.comparing(i -> i ^ h)).get())
                .collect(Collectors.toList());
    }

}
