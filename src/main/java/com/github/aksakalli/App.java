package com.github.aksakalli;

import com.github.aksakalli.handler.MinHasher;
import com.github.aksakalli.handler.MoleculeLoader;
import com.github.aksakalli.model.Molecule;
import com.github.aksakalli.util.Jaccard;
import com.github.aksakalli.util.Tanimoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Main flow of Graph Min Hash
 */
public class App {

    final private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.info("Welcome to Graph Min Hash!");

        MoleculeLoader moleculeLoader = new MoleculeLoader();
        MinHasher minHasher = new MinHasher();

        Molecule molecule1 = moleculeLoader.nextMolecule();
        logger.info("molecule1: " + molecule1.getStructureGraph().toString());
        Set<Integer> fingerprints1 = molecule1.getFingerprintSet();
        List<Integer> sketch1 = minHasher.getSketchFromFingerprintSet(fingerprints1);
        logger.info("sketch1: {}", Arrays.deepToString(sketch1.toArray()));

        Molecule molecule2 = moleculeLoader.nextMolecule();
        logger.info("molecule2: " + molecule2.getStructureGraph().toString());
        Set<Integer> fingerprints2 = molecule2.getFingerprintSet();
        List<Integer> sketch2 = minHasher.getSketchFromFingerprintSet(fingerprints2);
        logger.info("sketch2: {}", Arrays.deepToString(sketch2.toArray()));

        logger.info("Jaccard Distance of fingerprints: {}", Jaccard.calculateSimilarity(fingerprints1, fingerprints2));
        logger.info("Similarity of MinHash sketches: {}", Tanimoto.calculateSimilarity(sketch1, sketch2));
    }

}
