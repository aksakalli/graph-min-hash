package com.github.aksakalli.handler;

import com.github.aksakalli.model.AtomVertex;
import com.github.aksakalli.model.MoleculeGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Loads graphs from given or default file
 * <p>
 * File format:
 * # NSC Identifier, Class (Active - 2, Moderate - 1, Inactive -0), #Vertices, #Edges
 * AtomType, AtomType, ...
 * Bond (AtomId, AtomId, BondType), Bond, ...
 */
public class GraphLoader {

    final Logger logger = LoggerFactory.getLogger(GraphLoader.class);

    private static final String DEFAULT_DATASET_FILE = "datasets/AIDS99.txt";
    private Scanner scanner;

    /**
     * Reads from default file
     */
    public GraphLoader() {
        this(DEFAULT_DATASET_FILE);
    }

    /**
     *
     * @param filePath path to datasetfile, format should n
     */
    public GraphLoader(String filePath) {
        logger.info("Dataset file loading...");
        ClassLoader classLoader = getClass().getClassLoader();

        //TODO: handle exception
        File file = new File(classLoader.getResource(filePath).getFile());

        try {
            this.scanner = new Scanner(file);
            logger.info("Dataset scanner was initialized.");
        } catch (IOException e) {
            logger.error("Dataset file not found!");
            e.printStackTrace();
        }
    }

    public MoleculeGraph nextGraph() {

        if (!scanner.hasNextLine()) {
            logger.info("End of data stream file");
            scanner.close();
            return null;
        }

        // Reading meta info line
        String[] metaInfo = scanner.nextLine().split("\\s+");
        if (metaInfo[0].charAt(0) != '#') {
            logger.info("Not a molecule info line!");
            scanner.close();
            return null;
        }

        UndirectedGraph<AtomVertex, DefaultWeightedEdge> structureGraph
                = new SimpleGraph<>(DefaultWeightedEdge.class);
        List<AtomVertex> vertices = new ArrayList<>();

        // Reading atom line
        int[] atomIds = Pattern.compile("\\s+")
                .splitAsStream(scanner.nextLine())
                .mapToInt(Integer::parseInt)
                .toArray();

        IntStream.range(0, atomIds.length)
                .forEach(i -> {
                    AtomVertex vertex = new AtomVertex(i, atomIds[i]);
                    vertices.add(vertex);
                    structureGraph.addVertex(vertex);
                });

        // Reading bound line
        int[] boundInfo = Stream.of(scanner.nextLine().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        //IMPORTANT: index starts at 1 in bound info!
        IntStream.range(0, boundInfo.length / 3)
                .forEach(i -> structureGraph.addEdge(
                        vertices.get(boundInfo[i * 3] - 1),
                        vertices.get(boundInfo[i * 3 + 1] - 1)));

        return new MoleculeGraph(Integer.parseInt(metaInfo[1]), Integer.parseInt(metaInfo[2]), structureGraph);
    }

}
