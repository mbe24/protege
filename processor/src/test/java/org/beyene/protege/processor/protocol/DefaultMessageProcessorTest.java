package org.beyene.protege.processor.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Iterator;

import org.beyene.protege.core.Protocol;
import org.beyene.protege.core.Type;
import org.beyene.protege.core.Unit;
import org.beyene.protege.core.data.Composition;
import org.beyene.protege.core.data.DataUnit;
import org.beyene.protege.core.data.Primitive;
import org.beyene.protege.processor.HelloProtocol;
import org.beyene.protege.processor.util.ProtocolUtil;
import org.junit.Assert;
import org.junit.Test;

public class DefaultMessageProcessorTest {

    private final Protocol p = HelloProtocol.get();
    private final DefaultMessageProcessor mp = new DefaultMessageProcessor(p);
    private final UnitProcessor up = DefaultUnitProcessor.INSTANCE;

    @Test
        public void testRead() throws Exception {
    	DataUnit du = getMessage();
    	String versionId = p.getHeader().getConfiguration().getVersionId();
    	du.addPrimitiveValue(versionId, Type.INTEGER, Long.valueOf(1));
    	String unitId = ProtocolUtil.getUnitIdElement(p).getId();
    	du.addPrimitiveValue(unitId, Type.BYTE, new Byte[] { 1 });
    
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	mp.write(du, p, Channels.newChannel(os));
    
    	InputStream is = new ByteArrayInputStream(os.toByteArray());
    	DataUnit result = mp.read(p, Channels.newChannel(is));
    	
    	int version = result.getPrimitiveValue(versionId, Primitive.INTEGER).intValue();
    	Assert.assertEquals(1, version);
    	
    	byte unit = result.getPrimitiveValue(unitId, Primitive.BYTES)[0];
    	Assert.assertEquals(1, unit);
    	
    	Double averageAge = result.getPrimitiveValue("average-age", Primitive.DOUBLE);
    	Assert.assertNotNull(averageAge);
    	Assert.assertEquals(32.125d, averageAge, 0.00001);
    	
    	Iterator<Composition> personIt = result.getComplexCollection("persons").iterator();
    	Composition personMax = personIt.next();
    	Composition personJohn = personIt.next();
    	Assert.assertEquals(2, result.getComplexCollection("persons").size());
    	
    	Assert.assertEquals("Max", personMax.getPrimitiveValue("first-name", Primitive.STRING));
    	Assert.assertEquals("Mustermann", personMax.getPrimitiveValue("last-name", Primitive.STRING));
    	Assert.assertEquals("M", personMax.getPrimitiveValue("gender", Primitive.STRING));
    	
    	Assert.assertEquals("John", personJohn.getPrimitiveValue("first-name", Primitive.STRING));
    	Assert.assertEquals("Doe", personJohn.getPrimitiveValue("last-name", Primitive.STRING));
    	Assert.assertEquals("M", personJohn.getPrimitiveValue("gender", Primitive.STRING));
        }

    @Test
        public void testWrite() throws Exception {
    	DataUnit du = getMessage();
    	String versionId = p.getHeader().getConfiguration().getVersionId();
    	du.addPrimitiveValue(versionId, Type.INTEGER, Long.valueOf(1));
    	String unitId = ProtocolUtil.getUnitIdElement(p).getId();
    	du.addPrimitiveValue(unitId, Type.BYTE, new Byte[] { 1 });
    
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	int bytesWritten = mp.write(du, p, Channels.newChannel(os));
    	Assert.assertEquals(os.size(), bytesWritten);
    
        }

    private DataUnit getMessage() throws IOException {
	Protocol p = HelloProtocol.get();
	DataUnit du = new DataUnit();
	Unit reponse = null;
	for (Unit u : p.getUnits().getUnits())
	    if (u.getName().equals("hello-response"))
		reponse = u;
	Assert.assertNotNull(reponse);
	du.setUnit(reponse);

	byte[] bytes = DefaultUnitProcessorTest.createHelloResponseBody();
	ByteArrayInputStream is = new ByteArrayInputStream(bytes);
	du = up.fromStream(du, p, Channels.newChannel(is));
	return du;
    }
}