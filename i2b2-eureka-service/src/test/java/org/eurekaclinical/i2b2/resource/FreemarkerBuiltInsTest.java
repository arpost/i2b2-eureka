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
import freemarker.template.TemplateException;
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
public class FreemarkerBuiltInsTest {

	private FreemarkerBuiltIns freemarkerBuiltins;

	@Before
	public void setUp() {
		this.freemarkerBuiltins = new FreemarkerBuiltIns();
	}

	@After
	public void tearDown() {
		this.freemarkerBuiltins = null;
	}

	@Test
	public void testSimpleTrueTwoArg() throws TemplateException {
		Assert.assertEquals(true, Boolean.parseBoolean(freemarkerBuiltins.eval("true", null)));
	}
	
	@Test
	public void testSimpleTrueOneArg() throws TemplateException {
		Assert.assertEquals(true, Boolean.parseBoolean(freemarkerBuiltins.eval("true")));
	}

	@Test
	public void testSimpleFalseTwoArg() throws TemplateException {
		Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("false", null)));
	}
	
	@Test
	public void testSimpleFalseOneArg() throws TemplateException {
		Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("false")));
	}

	@Test
	public void testOneVariableTrue() throws TemplateException {
		Map<String, String> model = Collections.singletonMap("type", "staff");
		Assert.assertEquals(true, Boolean.parseBoolean(freemarkerBuiltins.eval("type != \"student\"", model)));
	}

	@Test
	public void testOneVariableFalse() throws TemplateException {
		Map<String, String> model = Collections.singletonMap("type", "student");
		Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("type != \"student\"", model)));
	}

	@Test
	public void testTwoVariablesTrue() throws TemplateException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "staff");
		model.put("organization", "Hanford University");
		Assert.assertEquals(true, Boolean.parseBoolean(freemarkerBuiltins.eval("type != \"student\" && organization == \"Hanford University\"", model)));
	}

	@Test
	public void testTwoVariablesFalse() throws TemplateException {
		Map<String, String> model = new HashMap<>();
		model.put("type", "student");
		model.put("organization", "Hanford University");
		Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("type != \"student\" && organization == \"Hanford University\"", model)));
	}

	@Test
	public void testParseErrorTwoArg() {
		try {
			Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("foo", null)));
			Assert.fail();
		} catch (TemplateException ex) {
		}
	}
	
	@Test
	public void testParseErrorOneArg() {
		try {
			Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval("foo")));
			Assert.fail();
		} catch (TemplateException ex) {
		}
	}
	
	@Test
	public void testNullExpressionTwoArg() {
		try {
			Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval(null, null)));
			Assert.fail();
		} catch (TemplateException ex) {
			Assert.fail();
		} catch (IllegalArgumentException iae) {
		}
	}
	
	@Test
	public void testNullExpressionOneArg() {
		try {
			Assert.assertEquals(false, Boolean.parseBoolean(freemarkerBuiltins.eval(null)));
			Assert.fail();
		} catch (TemplateException ex) {
			Assert.fail();
		} catch (IllegalArgumentException iae) {
		}
	}
}
