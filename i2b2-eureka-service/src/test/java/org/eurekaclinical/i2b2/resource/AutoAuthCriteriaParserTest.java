package org.eurekaclinical.i2b2.resource;

/*-
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 - 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrew Post
 */
public class AutoAuthCriteriaParserTest {

	private AutoAuthCriteriaParser parser;

	@Before
	public void setUp() {
		this.parser = new AutoAuthCriteriaParser();
	}

	@After
	public void tearDown() {
		this.parser = null;
	}

	@Test
	public void testSimpleTrue() throws CriteriaParseException {
		Assert.assertEquals(true, parser.parse("true", null));
	}

	@Test
	public void testSimpleFalse() throws CriteriaParseException {
		Assert.assertEquals(false, parser.parse("false", null));
	}

	@Test
	public void testOneVariableTrue() throws CriteriaParseException {
		Map<String, String> model = Collections.singletonMap("type", "staff");
		Assert.assertEquals(true, parser.parse("type != \"student\"", model));
	}

	@Test
	public void testOneVariableFalse() throws CriteriaParseException {
		Map<String, String> model = Collections.singletonMap("type", "student");
		Assert.assertEquals(false, parser.parse("type != \"student\"", model));
	}

	@Test
	public void testTwoVariablesTrue() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "staff");
		model.put("organization", "Hanford University");
		Assert.assertEquals(true, parser.parse("type != \"student\" && organization == \"Hanford University\"", model));
	}

	@Test
	public void testTwoVariablesFalse() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		model.put("organization", "Hanford University");
		Assert.assertEquals(false, parser.parse("type != \"student\" && organization == \"Hanford University\"", model));
	}

	@Test
	public void testParseError() {
		try {
			Assert.assertEquals(false, parser.parse("foo", null));
			Assert.fail();
		} catch (CriteriaParseException ex) {
		}
	}
	
	@Test
	public void testNullCriteria() throws CriteriaParseException {
		Assert.assertEquals(true, parser.parse(null, null));
	}
	
	@Test
	public void testNullVariableTrue() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		Assert.assertEquals(true, parser.parse("organization! == \"\"", model));
	}
	
	@Test
	public void testNullVariableFalse() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		Assert.assertEquals(false, parser.parse("organization! == \"Hanford University\"", model));
	}
	
	@Test
	public void testNullVariableTrue2() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		Assert.assertEquals(true, parser.parse("organization! != \"Hanford University\"", model));
	}
	
	@Test
	public void testNullVariableFalse2() throws CriteriaParseException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		model.put("organization", "Hanford University");
		Assert.assertEquals(false, parser.parse("organization! != \"Hanford University\"", model));
	}
}
