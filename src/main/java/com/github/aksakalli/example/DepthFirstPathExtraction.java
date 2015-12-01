package com.github.aksakalli.example;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.event.TraversalListenerAdapter;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import java.util.*;

/**
 * Simple Path extraction toy example with DFS [Ralaivola et al. 2005]
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
            // add new visited graph to path
            path.add(e.getVertex());
        }

        @Override
        public void vertexFinished(VertexTraversalEvent<String> e) {
            // remove finished vertex from the path
            path.remove(path.size() - 1);
        }

        public List<String> getPath() {
            return path;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        UndirectedGraph<String, DefaultEdge> g = createToyGraph();
        Set<String> vertices = g.vertexSet();

        for (String vertex : vertices) {
            GraphIterator<String, DefaultEdge> iterator = new DepthFirstIterator<>(g, vertex);
            MyListener myListener = new MyListener(g);
            iterator.addTraversalListener(myListener);

            System.out.println("Start Vertex: " + vertex);
            while (iterator.hasNext()) {
                iterator.next();
                System.out.println(Arrays.deepToString(myListener.getPath().toArray()));
            }
        }

        System.out.println("DFS with all paths and no cycles");
        for (String vertex : vertices) {
            System.out.println("Start Vertex: " + vertex);
            dfs(new ArrayList<>(), vertex, g);
        }
    }


    private static void dfs(List<String> path, String vertex, UndirectedGraph<String, DefaultEdge> g) {

        List<String> nextPath = new ArrayList<>();
        nextPath.addAll(path);
        nextPath.add(vertex);
        System.out.println(Arrays.deepToString(nextPath.toArray()));

        List<String> neighbors = Graphs.neighborListOf(g, vertex);
        for (String n : neighbors) {
            if (!nextPath.contains(n)) {
                dfs(nextPath, n, g);
            }
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
