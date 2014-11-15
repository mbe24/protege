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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.ComplexType;
import org.beyene.protege.core.Element;
import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.element.GenericElementProcessor;
import org.beyene.protege.processor.util.ByteUtil;
import org.beyene.protege.processor.util.ProtocolUtil;

public class DefaultHeaderProcessor implements HeaderProcessor {

    private final Element unitId;
    private final Map<String, Unit> units;

    private final GenericElementProcessor ep = GenericElementProcessor.INSTANCE;

    public DefaultHeaderProcessor(Protocol p) {
	this.unitId = ProtocolUtil.getUnitIdElement(p);

	Map<String, Unit> m = new HashMap<>();
	for (Unit unit : p.getUnits().getUnits()) {
	    byte[] id = unit.getKeyValue().getValue().getBytes();
	    m.put(ByteUtil.toHexString(id), unit);
	}
	this.units = Collections.unmodifiableMap(m);
    }

    @Override
    public DataUnit fromStream(Protocol p, InputStream is) throws IOException {
	ComplexType header = p.getHeader();

	DataUnit du = new DataUnit();
	for (Element e : header.getElements()) {
	    Object value = ep.fromStream(e, is);
	    du.addPrimitiveValue(e.getId(), e.getType(), value);
	}
	
	byte[] idValue = ByteUtil.unbox(du.getPrimitiveValue(unitId.getId(), Primitive.BYTES));
	du.setUnit(units.get(ByteUtil.toHexString(idValue)));
	return du;
    }

    @Override
    public int toStream(DataUnit du, Protocol p, OutputStream os) throws IOException {
	ComplexType header = p.getHeader();

	int bytesWritten = 0;
	for (Element e : header.getElements()) {
	    Object value = du.getPrimitiveValue(e.getId(), Primitive.forType(e.getType()));
	    bytesWritten += ep.toStream(value, e, os);
	}
	return bytesWritten;
    }
}