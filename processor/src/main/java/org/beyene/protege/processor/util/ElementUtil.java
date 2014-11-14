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

import org.beyene.protege.core.Element;
import org.beyene.protege.core.Length;
import org.beyene.protege.core.Value;

public final class ElementUtil {

    private ElementUtil() {
	// private constructor to prevent instantiation
    }
    
    public static int precedingLengthWritten(Element e) {
	int length = 0;
	if (e.getLength() != null && e.getLength().getBit() == null)
	    length = e.getLength().getPrecedingLengthFieldSize() / 8;
	return length;
    }
    
    public static boolean hasValue(Element e) {
	return e.getValue() != null;
    }
    
    public static Value getValue(Element e) {
	return e.getValue();
    }
    
    public static boolean hasLength(Element e) {
	return e.getLength() != null;
    }
    
    public static Length getLength(Element e) {
	return e.getLength();
    }
    
    public static boolean hasPrecedingLengthField(Element e) {
	return hasLength(e) && getLength(e).getPrecedingLengthFieldSize() != null;
    }
    
    public static int getPrecedingLengthFieldWidth(Element e) {
	return getLength(e).getPrecedingLengthFieldSize();
    }
    
    public static boolean hasFixedLength(Element e) {
	return hasLength(e) && getLength(e).getBit() != null;
    }
    
    public static int getFixedLength(Element e) {
	return getLength(e).getBit();
    }
}