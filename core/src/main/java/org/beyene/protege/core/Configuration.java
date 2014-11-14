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
package org.beyene.protege.core;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "config", propOrder = { "unitId", "totalLengthId", "versionId" })
public class Configuration {

    private String unitId;
    private String totalLengthId;
    private String versionId;

    @XmlElement(name = "unit-id", required = true)
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @XmlElement(name = "total-length-id")
    public String getTotalLengthId() {
	return totalLengthId;
    }

    public void setTotalLengthId(String totalLengthId) {
	this.totalLengthId = totalLengthId;
    }

    @XmlElement(name = "version-id")
    public String getVersionId() {
	return versionId;
    }

    public void setVersionId(String versionId) {
	this.versionId = versionId;
    }
}