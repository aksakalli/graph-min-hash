package com.github.aksakalli;

import com.github.aksakalli.handler.MinHasher;
import com.github.aksakalli.handler.MoleculeLoader;
import com.github.aksakalli.model.ExperimentMolecule;
import com.github.aksakalli.model.Molecule;
import com.github.aksakalli.util.Jaccard;
import com.github.aksakalli.util.Tanimoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Main flow of Graph Min Hash
 */
public class App {

    final private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Welcome to Graph Min Hash!");


        MoleculeLoader moleculeLoader = new MoleculeLoader();
        MinHasher minHasher = new MinHasher();
        List<ExperimentMolecule> molecules = new ArrayList<>();
        List<ExperimentMolecule> activeMolecules = new ArrayList<>();

        while (true) {
            Molecule molecule = moleculeLoader.nextMolecule();
            if (molecule == null) {
                break;
            }
            //logger.info("molecule: {}, class: {}", molecule.getNciId(), molecule.getClassification());
            List<Integer> sketch = minHasher.getSketchFromFingerprintSet(molecule.getFingerprintSet());
            //logger.info("sketch: {}", Arrays.deepToString(sketch.toArray()));

            ExperimentMolecule experimentMolecule = new ExperimentMolecule(molecule.getNciId(), molecule.getClassification(), sketch);
            molecules.add(experimentMolecule);
            if (experimentMolecule.getClassification() == 2) {
                activeMolecules.add(experimentMolecule);
            }
        }

        double accuracy = activeMolecules.stream()
                .map(am -> molecules.stream().sorted(Comparator.comparing(m -> 1 - am.getSimilarity(m)))
                        .limit(10)
                        .filter(m -> m.getClassification() == 2)
                        .count())
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();

        double tolerantAccuracy = activeMolecules.stream()
                .map(am -> molecules.stream().sorted(Comparator.comparing(m -> 1 - am.getSimilarity(m)))
                        .limit(10)
                        .filter(m -> m.getClassification() != 0)
                        .count())
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();
        //Active - 2, Moderate - 1, Inactive -0
        logger.info("total molecules: {}",molecules.size());
        logger.info("active molecules: {}",activeMolecules.size());
        logger.info("moderate molecules: {}",molecules.stream().filter(m -> m.getClassification() == 1).count());
        logger.info("accuracy: {}", accuracy);
        logger.info("tolerantAccuracy: {}", tolerantAccuracy);

    }


}
