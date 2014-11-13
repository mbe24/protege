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


import org.beyene.protege.core.Type;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DataBrokerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAddPrimitiveValue() throws Exception {
	DataBroker db = new DataBroker();

	Object o = Long.valueOf(5);
	boolean result = db.addPrimitiveValue("my-int", Type.INTEGER, o);
	Assert.assertTrue(result);
	
	result = db.addPrimitiveValue("my-int2", Type.INTEGER, o);
	Assert.assertTrue(result);
	
	result = db.addPrimitiveValue("my-int", Type.INTEGER, o);
	Assert.assertFalse(result);

	o = Integer.valueOf(5);

	thrown.expect(IllegalArgumentException.class);
	db.addPrimitiveValue("my-int", Type.INTEGER, o);
    }

    @Test
    public void testGetPrimitiveValue() throws Exception {
	DataBroker db = new DataBroker();

	Long l = Long.valueOf(5);
	Object o = l;
	boolean result = db.addPrimitiveValue("my-int", Type.INTEGER, o);
	Assert.assertTrue(result);
	
	Long retrieved = db.getPrimitiveValue("my-int", Primitive.INTEGER);
	Assert.assertEquals(l, retrieved);
	
	Object untypedLong = db.getPrimitiveValue("my-int", Primitive.forType(Type.INTEGER));
	Assert.assertTrue("Primitve comparison with unspecified generic type failed!", l.equals(untypedLong));
	
	Long none = db.getPrimitiveValue("none", Primitive.INTEGER);
	Assert.assertNull(none);
    }
}