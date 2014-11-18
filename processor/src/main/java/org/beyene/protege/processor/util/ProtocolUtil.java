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
package org.beyene.protege.processor.util;

import java.util.Arrays;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Configuration;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;

public final class ProtocolUtil {

    private ProtocolUtil() {
	// private constructor to prevent instantiation
    }

    public static int getHeaderBytes(Protocol p) {
	ComplexType header = p.getHeader();

	int bytes = 0;
	for (Element e : header.getElements())
	    if (ElementUtil.hasValue(e))
		bytes += ElementUtil.getValue(e).getHex().length() / 2;
	    else if (ElementUtil.hasFixedLength(e))
		bytes += (ElementUtil.getFixedLength(e));
	    else
		throw new IllegalStateException(String.format(
			"Element '%s' in protocol header has no fixed length!",
			e.getId()));

	return bytes;
    }

    public static boolean hasTotalLength(Protocol p) {
	return p.getHeader().getConfiguration().getTotalLengthId() != null;
    }
    
    public static int getTotalLength(DataUnit du, Protocol p) {
	ComplexType header = p.getHeader();
	Configuration config = header.getConfiguration();
	
	int totalLength = -1;
	if (config != null && config.getTotalLengthId() != null) {
	    try {
		Long l = du.getPrimitiveValue(config.getTotalLengthId(), Primitive.INTEGER);
		if (l != null)
		    totalLength = l.intValue();
	    } catch (IllegalArgumentException e) {
		// do nothing
	    }
	}
	
	return totalLength;
    }

    public static int getVersion(DataUnit du, Protocol p) {
	ComplexType header = p.getHeader();
	Configuration config = header.getConfiguration();
	
	int version = -1;
	if (config != null && config.getVersionId() != null) {
	    try {
		Long l = du.getPrimitiveValue(config.getVersionId(), Primitive.INTEGER);
		if (l != null)
		    version = l.intValue();
	    } catch (IllegalArgumentException e) {
		// do nothing
	    }
	}
	
	return version;
    }
    
    public static void addUnitInfo(DataUnit du, Protocol p, Element unitId) {
	if (Primitive.forType(unitId.getType()) == Primitive.BYTES)
	    throw new IllegalStateException(String.format("Type of unit id's value is %s instead of BYTE!", unitId.getType().name()));
	    
	byte[] id = ByteUtil.unbox(du.getPrimitiveValue(unitId.getId(), Primitive.BYTES));
	for (Unit unit : p.getUnits().getUnits()) {
	    byte[] currentId = unit.getKeyValue().getValue().getBytes();
	    if (Arrays.equals(id, currentId)) {
		du.setUnit(unit);
		break;
	    }
	}
    }
    
    public static Element getUnitIdElement(Protocol p) {
	ComplexType header = p.getHeader();
	Configuration config = header.getConfiguration();
	
	String unitId = config.getUnitId();
	Element unitIdElement = null;
	for(Element e :header.getElements())
	    if (e.getId().equals(unitId)) {
		unitIdElement = e;
		break;
	    }
	
	return unitIdElement;
    }
}