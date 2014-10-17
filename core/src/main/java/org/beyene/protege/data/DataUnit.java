/*
 * Copyright 2014 Mikael Beyene
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.beyene.protege.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUnit {

	private final String name;

	private List<Atom> atoms;
	private final Map<String, Atom> identifiableAtoms = new HashMap<String, Atom>();

	private List<Composition> compositions;
	private final Map<String, Composition> identifiableCompositions = new HashMap<String, Composition>();

	public DataUnit(String name, List<Atom> atoms, List<Composition> compositions) {
		this.name = name;
		this.atoms = atoms;
		this.compositions = compositions;
		
		for (Atom atom : atoms)
			if (atom.hasId())
				identifiableAtoms.put(atom.getId(), atom);
		
		for (Composition composition : compositions)
			identifiableCompositions.put(composition.getName(), composition);
	}
	
	public DataUnit(String name) {
		this(name, Collections.<Atom>emptyList(), Collections.<Composition>emptyList());
	}

	public String getName() {
		return name;
	}
	
	public List<Atom> getAtoms() {
		return atoms;
	}

	public void setAtoms(List<Atom> atoms) {
		this.atoms = atoms;
		
		identifiableAtoms.clear();
		for (Atom atom : atoms)
			if (atom.hasId())
				identifiableAtoms.put(atom.getId(), atom);
	}

	public List<Composition> getCompositions() {
		return compositions;
	}

	public void setCompositions(List<Composition> compositions) {
		this.compositions = compositions;
		
		identifiableCompositions.clear();
		for (Composition composition : compositions)
			identifiableCompositions.put(composition.getName(), composition);
	}

	public Atom getAtomById(String id) {
		return identifiableAtoms.get(id);
	}

	public Composition getCompositionById(String id) {
		return identifiableCompositions.get(id);
	}
}