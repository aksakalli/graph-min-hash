package com.github.aksakalli.handler;

import com.github.aksakalli.model.AtomVertex;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener to get fingerprint of paths
 */
public class MoleculeTraversalListener
        extends TraversalListenerAdapter<AtomVertex, DefaultEdge> {

    UndirectedGraph<AtomVertex, DefaultEdge> g;
    private List<AtomVertex> path;

    public MoleculeTraversalListener(UndirectedGraph<AtomVertex, DefaultEdge> g) {
        this.g = g;
        this.path = new ArrayList<>();
    }

    @Override
    public void vertexTraversed(VertexTraversalEvent<AtomVertex> e) {
        // add new visited graph to path
        path.add(e.getVertex());
    }

    @Override
    public void vertexFinished(VertexTraversalEvent<AtomVertex> e) {
        // remove finished vertex from the path
        path.remove(path.size() - 1);
    }

    public List<AtomVertex> getPath() {
        return path;
    }
}
