package com.github.aksakalli.model;

import com.github.aksakalli.handler.MoleculeTraversalListener;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Molecule modeled as Graph
 * <p>
 * Atoms = Vertices, Bounds = Edges
 * </p>
 */
public class Molecule {

    private static final int PATH_EXTRACTION_LIMIT = 10;

    private int nciId;
    private int classification;

    //Change DefaultWeightedEdge to BoundEdge!
    private UndirectedGraph<AtomVertex, DefaultEdge> structureGraph;

    public int getAtomCount() {
        return structureGraph.vertexSet().size();
    }

    public int getBoundCount() {
        return structureGraph.edgeSet().size();
    }

    public Molecule(int nciId, int classification, UndirectedGraph<AtomVertex, DefaultEdge> structureGraph) {
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

    public UndirectedGraph<AtomVertex, DefaultEdge> getStructureGraph() {
        return structureGraph;
    }

    /**
     * It extracts the graph in substructures and returns the set of
     * computed fingerprints. It uses DFS algorithm to get simple paths.
     *
     * @return set of all extracted substructures' fingerprints
     */
    public Set<Integer> getFingerprintSet() {
        Set<Integer> fingerprintSet = new TreeSet<>();

        MoleculeTraversalListener listener = new MoleculeTraversalListener(this.structureGraph);
        Set<AtomVertex> atoms = this.structureGraph.vertexSet();

        //extract paths starting from all atoms
        for (AtomVertex a : atoms) {
            //give root atom for each
            GraphIterator<AtomVertex, DefaultEdge> iterator = new DepthFirstIterator<>(this.structureGraph, a);
            iterator.addTraversalListener(listener);

            while (iterator.hasNext()) {
                iterator.next();
                List<AtomVertex> path = listener.getPath();
                if (path.size() <= PATH_EXTRACTION_LIMIT) {
                    //System.out.println(Arrays.deepToString(path.toArray()));
                    fingerprintSet.add(Arrays.deepHashCode(path.toArray()));
                }
            }
        }

        return fingerprintSet;
    }
}
