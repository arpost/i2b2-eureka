package edu.emory.bmi.aiw.i2b2export.config;

/*
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 Emory University
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

import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class I2b2EurekaServicesProperties extends AbstractProperties {
	
	@Override
	public String getProxyCallbackServer() {
		return this.getValue("i2b2eureka.services.callbackserver");
	}
	
	/**
	 * Gets the URL of the i2b2 proxy cell. Reads from the properties file first if necessary.
	 *
	 * @return the URL as a String
	 */
	public String getProxyUrl() {
		return this.getValue("i2b2eureka.proxyUrl");
	}

	/**
	 * Gets the URL of the i2b2 services. Reads from the properties file first if necessary.
	 *
	 * @return the URL as a String
	 */
	public String getI2b2ServiceHostUrl() {
		return this.getValue("i2b2eureka.serviceHostUrl");
	}
	
	public String getServiceUrl() {
		return this.getValue("eureka.services.url");
	}
	
	public String getSourceConfigId() {
		return this.getValue("i2b2eureka.sourceConfigId");
	}
	
}
