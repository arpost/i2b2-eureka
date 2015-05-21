package edu.emory.bmi.aiw.i2b2export.i2b2;

/*
 * #%L
 * i2b2 Export Service
 * %%
 * Copyright (C) 2013 Emory University
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for utility methods for communicating with i2b2.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public final class I2b2CommUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(I2b2CommUtil.class);

	/*
	 * the location of the project's i2b2 messaging XML templates
	 */
	static final String TEMPLATES_DIR = "i2b2-xml-templates";

	/*
	 * The i2b2 date format. The colon (:) must be stripped out of the timezone portion first.
	 */
	public static final String I2B2_DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	/**
	 * Private constructor to prevent instantiation
	 */
	private I2b2CommUtil() {
	}

	
}
