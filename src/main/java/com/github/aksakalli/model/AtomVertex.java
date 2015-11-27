package com.github.aksakalli.model;

import java.io.Serializable;

/**
 * Atom vertices for molecular structure
 * <p>
 * Atoms are represented as vertices in
 * graph representation of molecules.
 * </p>
 */
public class AtomVertex implements Serializable {

    private int position;
    private int atomId;

    public static final String[] ATOM_CODES = {"ERR", "H", "C", "O", "CU", "N", "S", "P", "CL", "ZN", "B", "BR", "CO", "MN", "AS", "AL", "NI", "SE", "SI", "V", "SN", "I", "F", "LI", "SB", "FE", "PD", "HG", "BI", "NA", "CA", "TI", "ZR", "HO", "GE", "PT", "RU", "RH", "CR", "GA", "K", "AG", "AU", "TB", "IR", "TE", "MG", "PB", "W", "CS", "MO", "RE", "CD", "OS", "PR", "ND", "SM", "GD", "YB", "ER", "U", "TL", "NB", "AC"};

    public int getPosition() {
        return position;
    }

    public int getAtomId() {
        return atomId;
    }

    public AtomVertex(int position, int atomId) {
        if (0 >= atomId || ATOM_CODES.length <= atomId) {
            throw new IndexOutOfBoundsException("AtomId: " + atomId + " is out of bounds!");
        }
        this.position = position;
        this.atomId = atomId;
    }

    public String getAtomCode() {
        return ATOM_CODES[atomId];
    }

    @Override
    public String toString() {
        return this.position + "." + this.getAtomCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtomVertex that = (AtomVertex) o;

        if (position != that.position) return false;
        return atomId == that.atomId;

    }

    @Override
    public int hashCode() {
//        int result = position;
//        result = 31 * result + atomId;
//        return result;

        // meaning of life
        return 42 * atomId;
    }
}
