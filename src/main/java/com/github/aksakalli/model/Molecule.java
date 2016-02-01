package com.github.aksakalli.model;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

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

    private Set<Integer> fingerprintSet;

    /**
     * It extracts the graph in substructures and returns the set of
     * computed fingerprints. It uses DFS algorithm to get simple paths.
     *
     * @return set of all extracted substructures' fingerprints
     */
    public Set<Integer> getFingerprintSet() {
        if (fingerprintSet != null) {
            return this.fingerprintSet;
        }

        fingerprintSet = new TreeSet<>();
        Set<AtomVertex> atoms = this.structureGraph.vertexSet();
        for (AtomVertex a : atoms) {
            dfsAllPathTravel(new ArrayList<>(), a);
        }

        return fingerprintSet;
    }

    private void dfsAllPathTravel(List<AtomVertex> path, AtomVertex vertex) {

        List<AtomVertex> nextPath = new ArrayList<>();
        nextPath.addAll(path);
        nextPath.add(vertex);


        int i = nextPath.size();
        for (AtomVertex a : nextPath) {
            i--;
            if (i < nextPath.size() / 2) {
                fingerprintSet.add(Arrays.deepHashCode(nextPath.toArray()));
                break;
            }
            if (a.hashCode() < nextPath.get(i).hashCode()) {
                Collections.reverse(nextPath);
                fingerprintSet.add(Arrays.deepHashCode(nextPath.toArray()));
                Collections.reverse(nextPath);
                break;
            }
        }

        //System.out.println(Arrays.deepToString(nextPath.toArray()));

        List<AtomVertex> neighbors = Graphs.neighborListOf(structureGraph, vertex);
        for (AtomVertex n : neighbors) {
            if (nextPath.size() <= PATH_EXTRACTION_LIMIT && !nextPath.contains(n)) {
                dfsAllPathTravel(nextPath, n);
            }
        }
    }
}
