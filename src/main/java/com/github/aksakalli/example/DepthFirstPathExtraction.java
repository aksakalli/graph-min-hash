package com.github.aksakalli.example;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple Path extraction toy example with DFS [Ralaivola et al. 2005]
 *
 */
public class DepthFirstPathExtraction {


    static class MyListener extends TraversalListenerAdapter<String, DefaultEdge> {

        UndirectedGraph<String, DefaultEdge> g;
        private List<String> path;


        public MyListener(UndirectedGraph<String, DefaultEdge> g) {
            this.g = g;
            this.path = new ArrayList<>();
        }


        @Override
        public void vertexTraversed(VertexTraversalEvent<String> e) {
            String vertex = e.getVertex();
            // add new visited graph to path
            path.add(vertex);
            printPath();
        }

        @Override
        public void vertexFinished(VertexTraversalEvent<String> e) {
            // remove finished vertex from the path
            path.remove(path.size() - 1);
        }

        private void printPath() {
            System.out.println(Arrays.deepToString(path.toArray()));
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UndirectedGraph<String, DefaultEdge> g = createToyGraph();


        GraphIterator<String, DefaultEdge> iterator = new DepthFirstIterator<>(g, "B");
        iterator.addTraversalListener(new MyListener(g));

        while (iterator.hasNext()) {
            iterator.next();
        }
    }


    /**
     * Creates toy example at section 3.2
     *
     * @return a toy graph.
     */
    private static UndirectedGraph<String, DefaultEdge> createToyGraph() {
        UndirectedGraph<String, DefaultEdge> g =
                new SimpleGraph<>(DefaultEdge.class);


        g.addVertex("A");
        g.addVertex("B");
        g.addVertex("C");
        g.addVertex("D");
        g.addVertex("E");

        g.addEdge("A", "B");
        g.addEdge("B", "D");
        g.addEdge("D", "E");
        g.addEdge("D", "C");
        g.addEdge("C", "A");

        return g;
    }
}
