package com.github.aksakalli;

import com.github.aksakalli.handler.MinHasher;
import com.github.aksakalli.handler.MoleculeLoader;
import com.github.aksakalli.model.ExperimentMolecule;
import com.github.aksakalli.model.Molecule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Main flow of Graph Min Hash
 */
public class App {

    final private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Welcome to Graph Min Hash!");
        simplePathExperiment();
    }


    static void simplePathExperiment() {
        MoleculeLoader moleculeLoader = new MoleculeLoader();
        MinHasher minHasher = new MinHasher(64);
        List<ExperimentMolecule> molecules = new ArrayList<>();
//        List<ExperimentMolecule> activeMolecules = new ArrayList<>();
        long totalFingerprints =0;
        long toalVertex =0;
        long totalEdge=0;
        while (true) {
            Molecule molecule = moleculeLoader.nextMolecule();
            if (molecule == null) {
                break;
            }
            //logger.info("molecule: {}, class: {}", molecule.getNciId(), molecule.getClassification());

            Set<Integer> fingerprintSet = molecule.getFingerprintSet();
            totalFingerprints += fingerprintSet.size();
            toalVertex += molecule.getAtomCount();
            totalEdge += molecule.getBoundCount();
            //skip molecules which has less number of atoms than limited shingle size
            if (fingerprintSet.size() == 0) {
                continue;
            }
            List<Integer> sketch = minHasher.getSketchFromFingerprintSet(fingerprintSet);
            //logger.info("sketch: {}", Arrays.deepToString(sketch.toArray()));

            ExperimentMolecule experimentMolecule = new ExperimentMolecule(molecule.getNciId(), molecule.getClassification(), sketch);
            molecules.add(experimentMolecule);
//            if (experimentMolecule.getClassification() == 2) {
//                activeMolecules.add(experimentMolecule);
//            }
        }
        logger.info("totalFingerprints={}, toalVertex={}, totalEdge={}", totalFingerprints, toalVertex, totalEdge);
        int total=molecules.size();
        logger.info("totalFingerprints={}, toalVertex={}, totalEdge={}", (float)totalFingerprints/total, (float)toalVertex/total, (float)totalEdge/total);

        int tn = 0, tp = 0, fp = 0, fn = 0;
        for (ExperimentMolecule molecule : molecules) {

            long activeCount = molecules.parallelStream().sorted(Comparator.comparing(m -> 1 - molecule.getSimilarity(m)))
                    .limit(6)
                    .filter(m -> m.getNciId() != molecule.getNciId())
                    .limit(3)
                    .filter(m -> m.getClassification() == 2)
                    .count();

            if (activeCount > 1) {
                if (molecule.getClassification() == 2) {
                    tp++;
                } else {
                    fp++;
                }
            } else {
                if (molecule.getClassification() == 2) {
                    fn++;
                } else {
                    tn++;
                }
            }
        }

        logger.info("classification accuracy: {}", ((float) (tn + tp) / (fn + fp + tn + tp)));
//
//        List<Double> accuracyGraph = IntStream.range(1, 100).mapToObj(i ->
//                activeMolecules.stream()
//                        .map(am -> molecules.stream().sorted(Comparator.comparing(m -> 1 - am.getSimilarity(m)))
//                                .limit(i)
//                                .filter(m -> m.getClassification() == 2)
//                                .count())
//                        .mapToDouble(a -> a)
//                        .average()
//                        .getAsDouble())
//                .collect(Collectors.toList());
//        logger.info("accuracy: {}", Arrays.toString(accuracyGraph.toArray()));

//        double accuracy = activeMolecules.stream()
//                .map(am -> molecules.stream().sorted(Comparator.comparing(m -> 1 - am.getSimilarity(m)))
//                        .limit(10)
//                        .filter(m -> m.getClassification() == 2)
//                        .count())
//                .mapToDouble(a -> a)
//                .average()
//                .getAsDouble();
//        double tolerantAccuracy = activeMolecules.stream()
//                .map(am -> molecules.stream().sorted(Comparator.comparing(m -> 1 - am.getSimilarity(m)))
//                        .limit(10)
//                        .filter(m -> m.getClassification() != 0)
//                        .count())
//                .mapToDouble(a -> a)
//                .average()
//                .getAsDouble();

        //Active - 2, Moderate - 1, Inactive -0
        logger.info("fn={}, fp={}, tn={}, tp={}, acc={}", fn, fp, tn, tp, ((float) (tn + tp) / (fn + fp + tn + tp)));



        logger.info("total molecules: {}", molecules.size());
//        logger.info("active molecules: {}", activeMolecules.size());
//        logger.info("moderate molecules: {}", molecules.stream().filter(m -> m.getClassification() == 1).count());
//        logger.info("accuracy: {}", accuracy);
        //logger.info("tolerantAccuracy: {}", tolerantAccuracy);
    }

}
