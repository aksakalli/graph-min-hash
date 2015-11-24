package com.github.aksakalli;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        UndirectedGraph<String, DefaultEdge> stringGraph = createStringGraph();

        BronKerboschCliqueFinder  cliqueFinder = new BronKerboschCliqueFinder(stringGraph);
        cliqueFinder.getAllMaximalCliques().stream()
                .forEach(g -> System.out.println(g.toString()));

        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println(stringGraph.toString());
    }

    /**
     * Create a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static UndirectedGraph<String, DefaultEdge> createStringGraph()
    {
        UndirectedGraph<String, DefaultEdge> g =
                new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v1);
        g.addVertex(v2);


        // add edges to create a circuit

        g.addEdge(v3, v4);
        g.addEdge(v4, v1);
        g.addEdge(v2, v4);

        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        return g;
    }

}
