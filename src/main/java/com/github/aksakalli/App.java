package com.github.aksakalli;

import com.github.aksakalli.handler.GraphLoader;
import com.github.aksakalli.model.MoleculeGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main flow of Graph Min Hash
 */
public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        logger.info("Welcome to Graph Min Hash!");

        GraphLoader graphLoader = new GraphLoader();

        MoleculeGraph molecule = graphLoader.nextGraph();
        logger.info("The retrieved molecule: " + molecule.getStructureGraph().toString());
    }

}
