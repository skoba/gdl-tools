package se.cambio.cds.gdl.parser;

import java.io.InputStream;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.openehr.rm.datatypes.text.CodePhrase;

import se.cambio.cds.gdl.model.Guide;

public class ParseTermBindingTest extends ExpressionTestBase {

	public void setUp() {
		parser = new GDLParser();
	}
	
	public void testParseSimpleGDL() throws Exception {
		parse("high_aciclovir.v1.gdl");
		JXPathContext context = JXPathContext.newContext(guide);
		
		
		assertEquals(new CodePhrase("ATC", "J05AB01"), 
				context.getValue("/ontology/termBindings/ATC/bindings/gt0010/codes[1]"));
		
		try {
			context.getValue("/ontology/termBindings/ATC/bindings/gt0010/codes[2]");
			fail("Exception should be thrown at path pointing at the 2nd, duplicated element");
		} catch(Exception e) {
			assertTrue(e instanceof JXPathNotFoundException);
		}
	}
	
	private void parse(String input) throws Exception {
		guide = parser.parse(load(input));
	}
	
	private InputStream load(String name) throws Exception {
		return this.getClass().getClassLoader()
				.getResourceAsStream(name);
	}
	
	GDLParser parser;
	Guide guide;
}
/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *
 *  The Initial Developers of the Original Code are Iago Corbal and Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2012-2013
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */