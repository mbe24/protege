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
package org.beyene.protege.core.encoding;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.beyene.protege.core.data.Primitive;

@XmlType(name = "string-encoding")
public enum StringEncoding implements Encoding<String> {

    @XmlEnumValue("utf-8")
    UTF_8("UTF-8", 8),

    @XmlEnumValue("utf-16le")
    UTF_16LE("UTF-16LE", 16),

    @XmlEnumValue("utf-16be")
    UTF_16BE("UTF-16BE", 16);

    private final String charset;
    private final int bits;

    private StringEncoding(String charset, int bits) {
	this.charset = charset;
	this.bits = bits;
    }

    @Override
    public String getKey() {
	return charset;
    }

    @Override
    public Primitive<String> getPrimitive() {
	return Primitive.STRING;
    }

    @Override
    public int getWidth() {
	return bits;
    }
}