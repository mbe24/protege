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
package org.beyene.protege.core.data;

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