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
package org.beyene.protege.processor.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.Composition;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.HeterogeneousContainer;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.core.encoding.IntegerEncoding;
import org.beyene.protege.processor.atom.AtomProcessor;
import org.beyene.protege.processor.atom.AtomProcessorFactory;
import org.beyene.protege.processor.element.GenericElementProcessor;
import org.beyene.protege.processor.util.ElementUtil;
import org.beyene.protege.processor.util.IoUtil;

public enum DefaultUnitProcessor implements UnitProcessor {

    INSTANCE;

    private final GenericElementProcessor ep = GenericElementProcessor.INSTANCE;

    @Override
    public DataUnit fromStream(DataUnit du, Protocol p, InputStream is) throws IOException {
	List<Element> elements = new LinkedList<>(du.getUnit().getBody().getElements());

	/*
	 * stack that manages datacontainer per 'level', the level is increased
	 * every time we encounter a complex type
	 */
	Deque<HeterogeneousContainer> containerPerLevel = new ArrayDeque<>();
	containerPerLevel.offerFirst(du);

	/*
	 * stack that manages elements per level
	 */
	Deque<Integer> elementsPerLevel = new ArrayDeque<>();
	elementsPerLevel.offerFirst(elements.size());

	ListIterator<Element> it = elements.listIterator();
	HeterogeneousContainer data = containerPerLevel.peekFirst();
	while (it.hasNext()) {
	    Element e = it.next();
	    data = containerPerLevel.peekFirst();
	    
	    // get #elements that are left to process for current level and
	    // decrease value
	    int elementsLeft = elementsPerLevel.pollFirst();
	    elementsLeft--;
	    elementsPerLevel.offerFirst(elementsLeft);

	    // if no elements are left, go down a level in next loop
	    if (elementsLeft == 0) {
		containerPerLevel.pollFirst();
		elementsLeft = elementsPerLevel.pollFirst();
	    }
	    
	    // primitive
	    if (e.getType() != null) {
		Object value = ep.fromStream(e, is);
		data.addPrimitiveValue(e.getId(), e.getType(), value);
	    }
	    // complex type
	    else {
		ComplexType ct = getComplexType(e.getClassification(), p, du.getUnit());

		int occurrences = 1;
		// check if there are multiple occurrences
		if (ElementUtil.hasPrecedingLengthField(e)) {
		    int width = ElementUtil.getPrecedingLengthFieldWidth(e);
		    AtomProcessor<Long> ap = AtomProcessorFactory.getProcessor(Primitive.INTEGER);
		    occurrences = ap.interpret(IoUtil.readBytes(width / 8, is), IntegerEncoding.UNSIGNED).intValue();
		}

		int elementsAdded = addElements(e, ct, occurrences, it);
		if (elementsAdded > 0) {
		    elementsLeft = elementsPerLevel.pollFirst();
		    elementsLeft += occurrences - 1;
		    elementsPerLevel.offerFirst(elementsLeft);
		    
		    elementsPerLevel.offerFirst(elementsAdded);

		    Composition nextLevelContainer = new Composition();
		    nextLevelContainer.setId(e.getId());
		    
		    data.addComplexObject(e.getId(), nextLevelContainer);

		    data = nextLevelContainer;
		    containerPerLevel.offerFirst(data);
		} else {
		    // rather empty composition than null
		    data.addComplexObject(e.getId(), new Composition());
		}
	    }
	}
	return du;
    }

    @Override
    public int toStream(DataUnit du, Protocol p, OutputStream os)
	    throws IOException {
	// TODO Auto-generated method stub
	return 0;
    }

    // returns elements of complex type
    private int addElements(Element ce, ComplexType complexType, int occurrences, ListIterator<Element> it) {
	Element cenp = new Element();
	cenp.setClassification(ce.getClassification());
	cenp.setId(ce.getId());
	
	// only add elements for new level...
	List<Element> elements = complexType.getElements();
	int added = 0;
	if (occurrences > 0) {
	    for (Element e : elements) {
		it.add(e);
		added++;
	    }
	}
	
	/*
	 * the other occurrences of complex type are just added,
	 * since they get their own level later own.
	 * 
	 * NOTE: element cenp is copy of ce, with length info removed
	 */
	for (int i = 0; i < occurrences - 1; i++) {
	    it.add(cenp);
	    added++;
	}
	    
	
	// reset iterator position
	for (int i = 0; i < added; i++)
	    it.previous();

	return elements.size();
    }

    private ComplexType getComplexType(String name, Protocol p, Unit u) throws IllegalArgumentException {
	if (u.getComplexTypes() != null)
	    for (ComplexType ct : u.getComplexTypes())
		if (name.equals(ct.getName()))
		    return ct;
	return p.getType(name);
    }
}