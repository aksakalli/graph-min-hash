package com.github.aksakalli.model;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Molecule modeled as Graph
 * <p>
 * Atoms = Vertices, Bounds = Edges
 * </p>
 */
public class MoleculeGraph {

    private int nciId;
    private int classification;

    //Change DefaultWeightedEdge to BoundEdge!
    private UndirectedGraph<AtomVertex, DefaultWeightedEdge> structureGraph;

    public int getAtomCount (){
        return structureGraph.vertexSet().size();
    }

    public int getBoundCount (){
        return structureGraph.edgeSet().size();
    }

    public MoleculeGraph(int nciId, int classification, UndirectedGraph<AtomVertex, DefaultWeightedEdge> structureGraph) {
        this.nciId = nciId;
        this.classification = classification;
        this.structureGraph = structureGraph;
    }

    public int getNciId() {
        return nciId;
    }

    public int getClassification() {
        return classification;
    }

    public UndirectedGraph<AtomVertex, DefaultWeightedEdge> getStructureGraph() {
        return structureGraph;
    }
}
