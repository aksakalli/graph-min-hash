package com.github.aksakalli;

import com.github.aksakalli.handler.MinHasher;
import com.github.aksakalli.handler.MoleculeLoader;
import com.github.aksakalli.model.Molecule;
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

        Molecule molecule = moleculeLoader.nextMolecule();
        logger.info("The retrieved molecule: " + molecule.getStructureGraph().toString());

        Set<Integer> fingerprints = molecule.getFingerprintSet();
        List<Integer> sketch = minHasher.getSketchFromFingerprintSet(fingerprints);

        logger.info("sketch: {}", Arrays.deepToString(sketch.toArray()));
    }

}
