package com.github.aksakalli.model;

import com.github.aksakalli.util.Tanimoto;

import java.util.List;

/**
 * Molecule object used for experiment results, no need to store structure.
 * TODO: make it ObjectOriented
 */
public class ExperimentMolecule {
    private int nciId;
    private int classification;
    List<Integer> sketch;

    public ExperimentMolecule(int nciId, int classification, List<Integer> sketch) {
        this.nciId = nciId;
        this.classification = classification;
        this.sketch = sketch;
    }

    public int getNciId() {
        return nciId;
    }

    public int getClassification() {
        return classification;
    }

    public Double getSimilarity(ExperimentMolecule other) {
        return Tanimoto.calculateSimilarity(this.sketch, other.sketch);
    }
}
