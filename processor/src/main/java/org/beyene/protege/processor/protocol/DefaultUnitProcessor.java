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
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
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
    public DataUnit fromStream(DataUnit du, Protocol p, ReadableByteChannel channel) throws IOException {
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
	while (it.hasNext()) {
	    Element e = it.next();
	    HeterogeneousContainer data = containerPerLevel.peekFirst();
	    int elementsLeft = elementsPerLevel.peekFirst();

	    
	    // get #elements that are left to process for current level and
	    // decrease value
	    elementsLeft = elementsPerLevel.pollFirst();
	    elementsLeft--;
	    elementsPerLevel.offerFirst(elementsLeft);
	    
	    // if no elements are left, go down a level in next loop
	    if (elementsLeft <= 0) {
		containerPerLevel.pollFirst();
		elementsPerLevel.pollFirst();
	    }
	    
	    // primitive
	    if (e.getType() != null) {
		Object value = ep.read(e, channel);
		data.addPrimitiveValue(e.getId(), e.getType(), value);
	    }
	    // complex type
	    else {
		ComplexType ct = getComplexType(e.getClassification(), p, du.getUnit());

		// TODO read occurrences from element if they are fix 
		int occurrences = 1;
		// check if there are multiple occurrences
		if (ElementUtil.hasPrecedingLengthField(e)) {
		    int width = ElementUtil.getPrecedingLengthFieldWidth(e);
		    AtomProcessor<Long> ap = AtomProcessorFactory.getProcessor(Primitive.INTEGER);
		    occurrences = ap.interpret(IoUtil.readBytes(width / 8, channel), IntegerEncoding.UNSIGNED).intValue();
		} else if (ElementUtil.hasFixedLength(e))
		    occurrences = ElementUtil.getFixedLength(e);

		int elementsAdded = addElements(e, ct, occurrences, it);
		if (elementsAdded > 0) {
		    List<Composition> compositions = new ArrayList<>(occurrences);
		    for (int i = 0; i < occurrences; i++) {
			Composition c = new Composition();
			c.setId(e.getId());
			compositions.add(c);
			data.addComplexObject(e.getId(), c);
		    }
		    
		    /*
		     * reverse order, so that first composition is on top of stack
		     */
		    Collections.reverse(compositions);
		    for (Composition c : compositions) {
			containerPerLevel.offerFirst(c);
			elementsPerLevel.offerFirst(ct.getElements().size());
		    }
		}
	    }
	}
	return du;
    }

    @Override
    public int toStream(DataUnit du, Protocol p, WritableByteChannel channel) throws IOException {
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
	int bytesWritten = 0;
	while (it.hasNext()) {
	    Element e = it.next();
	    HeterogeneousContainer data = containerPerLevel.peekFirst();
	    int elementsLeft = elementsPerLevel.peekFirst();

	    // get #elements that are left to process for current level and
	    // decrease value
	    elementsLeft = elementsPerLevel.pollFirst();
	    elementsLeft--;
	    elementsPerLevel.offerFirst(elementsLeft);
	    
	    // if no elements are left, go down a level in next loop
	    if (elementsLeft <= 0) {
		containerPerLevel.pollFirst();
		elementsPerLevel.pollFirst();
	    }

	    // primitive
	    if (e.getType() != null) {
		Object value = data.getPrimitiveValue(e.getId(), Primitive.forType(e.getType()));
		bytesWritten += ep.write(value, e, channel);
	    }
	    // complex type
	    else {
		ComplexType ct = getComplexType(e.getClassification(), p, du.getUnit());
		List<Composition> objects = data.getComplexCollection(e.getId());

		int occurrences = objects.size();
		// check if there are multiple occurrences
		if (ElementUtil.hasPrecedingLengthField(e)) {
		    int width = ElementUtil.getPrecedingLengthFieldWidth(e);
		    AtomProcessor<Long> ap = AtomProcessorFactory.getProcessor(Primitive.INTEGER);
		    byte[] bytes = ap.toBytes(Long.valueOf(occurrences), IntegerEncoding.UNSIGNED, width);
		    bytesWritten += IoUtil.writeBytes(bytes, channel);
		} else if (ElementUtil.hasFixedLength(e)) {
		    occurrences = ElementUtil.getFixedLength(e);
		    // TODO error handling
		}

		int elementsAdded = addElements(e, ct, occurrences, it);
		if (elementsAdded > 0) {
		    List<Composition> compositions = data.getComplexCollection(e.getId());
		    compositions.subList(0, occurrences);
		    
		    /*
		     * reverse order, so that first composition is on top of stack
		     */
		    Collections.reverse(compositions);
		    for (Composition c : compositions) {
			containerPerLevel.offerFirst(c);
			elementsPerLevel.offerFirst(ct.getElements().size());
		    }
		}
	    }
	}
	return bytesWritten;
    }

    private int addElements(Element ce, ComplexType complexType, int occurrences, ListIterator<Element> it) {
	List<Element> elements = complexType.getElements();
	int added = 0;
	for (int i = 0; i < occurrences; i++) {
	    for (Element e : elements) {
		it.add(e);
		added++;
	    }
	}

	// reset iterator position
	for (int i = 0; i < added; i++)
	    it.previous();

	return added;
    }

    private ComplexType getComplexType(String name, Protocol p, Unit u)
	    throws IllegalArgumentException {
	if (u.getComplexTypes() != null)
	    for (ComplexType ct : u.getComplexTypes())
		if (name.equals(ct.getName()))
		    return ct;
	return p.getType(name);
    }
}