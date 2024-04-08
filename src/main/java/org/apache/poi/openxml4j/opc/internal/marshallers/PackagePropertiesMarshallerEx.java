/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.openxml4j.opc.internal.marshallers;

import java.io.OutputStream;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.w3c.dom.Document;

/**
 * Package core properties marshaller specialized for zipped package.
 */
public final class PackagePropertiesMarshallerEx extends PackagePropertiesMarshaller {

	@Override
	public boolean marshall(PackagePart part, OutputStream out) throws OpenXML4JException {

		// Saving the part in the zip file
		//String file = ZipPackage.TEMP_PATH + part.getPartName().getURI().toString();
		// Save in ZIP
		super.marshall(part, out); // Marshall the properties inside a XML
		// Document
		
		//XmlUtil.toFile(xmlDoc, file);
		return true;
	}
	
	public Document marshall(PackagePart part) throws OpenXML4JException{
		super.marshall(part,null);
		return xmlDoc; // Marshall the properties inside a XML
	}
}
