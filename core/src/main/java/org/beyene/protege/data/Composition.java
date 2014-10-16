package org.beyene.protege.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Composition {

	private final String name;
	private List<Atom> atoms;

	private final Map<String, Atom> identifiable = new HashMap<String, Atom>();

	public Composition(String name, List<Atom> atoms) {
		this.name = name;
		this.atoms = atoms;

		for (Atom atom : atoms)
			if (atom.hasId())
				identifiable.put(atom.getId(), atom);
	}
	
	public Composition(String name) {
		this(name, Collections.<Atom>emptyList());
	}

	public String getName() {
		return name;
	}

	public Atom getAtomById(String id) {
		return identifiable.get(id);
	}

	public List<Atom> getAtoms() {
		return atoms;
	}

	public void setAtoms(List<Atom> atoms) {
		this.atoms = atoms;
		
		identifiable.clear();
		for (Atom atom : atoms)
			if (atom.hasId())
				identifiable.put(atom.getId(), atom);
	}
}