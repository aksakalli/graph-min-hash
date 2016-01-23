package com.github.aksakalli.model;

import java.io.Serializable;

/**
 * Bound edges for molecular structure
 * <p>
 * Bounds between atoms are represented
 * as edges in graph structure. It is just
 * storing bound type, later on it might be extended.
 * </p>
 */
public class BoundEdge implements Serializable {

    private int bondType;

    //TODO: check constrains of bounds
    public BoundEdge(int bondType) {
        this.bondType = bondType;
    }

    public int getBondType() {
        return bondType;
    }

    @Override
    public String toString() {
        return "" + bondType;
    }


}
