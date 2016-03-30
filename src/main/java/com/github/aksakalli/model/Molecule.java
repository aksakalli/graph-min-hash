package com.github.aksakalli.model;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;

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
	private UndirectedGraph<AtomVertex, BoundEdge> structureGraph;

	public int getAtomCount() {
		return structureGraph.vertexSet().size();
	}

	public int getBoundCount() {
		return structureGraph.edgeSet().size();
	}

	public Molecule(int nciId, int classification, UndirectedGraph<AtomVertex, BoundEdge> structureGraph) {
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

	public UndirectedGraph<AtomVertex, BoundEdge> getStructureGraph() {
		return structureGraph;
	}

	private Set<Integer> fingerprintSet;

	public Set<Integer> getTripletFingerprints() {
		HashSet<Integer> fingerPrints = new HashSet<>();

		ArrayList<AtomVertex> vertices = new ArrayList<>(structureGraph.vertexSet());
		int n = this.getAtomCount();
		// enumerate triplets of vertices
		for (int i = 0; i<n-2; ++i) {
			for (int j=i+1; j<n-1; ++j) {
				for (int k=j+1; k<n; ++k) {
					AtomVertex u = vertices.get(i);
					AtomVertex v = vertices.get(j);
					AtomVertex w = vertices.get(k);
					BoundEdge uv = structureGraph.getEdge(u, v);
					BoundEdge vw = structureGraph.getEdge(v, w);
					BoundEdge wu = structureGraph.getEdge(w, u);
					fingerPrints.add(getFingerPrintOfTriple(u, v, w, uv, vw, wu));
					//					fingerPrints.add(getFingerPrintOfTripleFast(u, v, w, null, null, null));
				}
			}
		}

		return fingerPrints;
	}

	/**
	 * 
	 * @param u
	 * @param v
	 * @param w
	 * @param uv
	 * @param vw
	 * @param wu
	 * @return
	 */
	public static int getFingerPrintOfTriple(AtomVertex u, AtomVertex v, AtomVertex w, BoundEdge uv, BoundEdge vw, BoundEdge wu) {
		LinkedList<String> path = new LinkedList<>();

		path.add(u.getAtomCode());
		if (uv != null) {
			path.add(Integer.toString(uv.getBondType()));
		} else {
			path.add("null");
		}

		path.add(v.getAtomCode());
		if (vw != null) {
			path.add(Integer.toString(vw.getBondType()));
		} else {
			path.add("null");
		}

		path.add(w.getAtomCode());
		if (wu != null) {
			path.add(Integer.toString(wu.getBondType()));
		} else {
			path.add("null");
		}	

		LinkedList<String> reverse = new LinkedList<String>(path);
		Collections.reverse(reverse);

		int fingerprint = Integer.MAX_VALUE;
		for (int i=0; i<3; ++i) {
			int pathHash = String.join(" ", path).hashCode();
			int reverseHash = String.join(" ", reverse).hashCode();
			if (fingerprint > pathHash) {
				fingerprint = pathHash;
			}
			if (fingerprint > reverseHash) {
				fingerprint = reverseHash;
			}
			// put next vertex up front
			path.add(path.removeFirst());
			path.add(path.removeFirst());
			reverse.add(reverse.removeFirst());
			reverse.add(reverse.removeFirst());
		}
		return fingerprint;
	}

	public static int getFingerPrintOfTripleFast(AtomVertex u, AtomVertex v, AtomVertex w, BoundEdge uv, BoundEdge vw, BoundEdge wu) {
		int fingerprint = 0;

		fingerprint += u.getAtomId();
		if (uv != null) {
			fingerprint += uv.getBondType();
		} 

		fingerprint += v.getAtomId();
		if (vw != null) {
			fingerprint += vw.getBondType();
		}

		fingerprint += w.getAtomId();
		if (wu != null) {
			fingerprint += wu.getBondType();
		}
		return fingerprint;
	}


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


		fingerprintSet.add(Arrays.deepHashCode(nextPath.toArray()));
		//        fingerprintSet.add(fingerprintAtomChain(nextPath));
		//System.out.println(Arrays.deepToString(nextPath.toArray()));

		List<AtomVertex> neighbors = Graphs.neighborListOf(structureGraph, vertex);
		for (AtomVertex n : neighbors) {
			if (nextPath.size() <= PATH_EXTRACTION_LIMIT && !nextPath.contains(n)) {
				dfsAllPathTravel(nextPath, n);
			}
		}
	}

	private int fingerprintAtomChain(List<AtomVertex> path) {
		if (path == null)
			return 0;

		int result = 1;
		AtomVertex previousAtom = null;

		for (AtomVertex a : path) {
			if (previousAtom != null) {
				BoundEdge bound = structureGraph.getEdge(previousAtom, a);
				result = 31 * result + (bound == null ? 0 : bound.getBondType()) + 63;
			}

			result = 31 * result + a.getAtomId();
			previousAtom = a;
		}


		return result;
	}

	private Set<Integer> combinationfingerprints;

	public Set<Integer> getCombinationFingerprintSet() {

		combinationfingerprints = new TreeSet<>();
		combination(new ArrayList<>(), this.structureGraph.vertexSet());
		return combinationfingerprints;
	}

	private void combination(List<AtomVertex> path, Set<AtomVertex> atoms) {

		if (path.size() == 4) {
			combinationfingerprints.add(fingerprintAtomChain(path));
			return;
		}

		for (AtomVertex a : atoms) {

			//add to combination list
			List<AtomVertex> nextPath = new ArrayList<>();
			nextPath.addAll(path);
			nextPath.add(a);

			//remove from all set
			Set<AtomVertex> nextAtoms = new HashSet<>();
			nextAtoms.addAll(atoms);
			nextAtoms.remove(a);

			combination(nextPath, nextAtoms);

		}
	}

}
