package org.eurekaclinical.i2b2.provider;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import com.google.inject.Provider;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eurekaclinical.eureka.client.EurekaClient;
import org.eurekaclinical.i2b2.props.I2b2EurekaServicesProperties;

/**
 * 
 * @author Andrew Post
 */
@Singleton
public class ServicesClientProvider implements Provider<EurekaClient> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServicesClientProvider.class);
	private final EurekaClient client;

	@Inject
	public ServicesClientProvider(I2b2EurekaServicesProperties inProperties) {
		LOGGER.debug("service url = {}", inProperties.getServiceUrl());
		this.client = new EurekaClient(inProperties.getServiceUrl());
	}

	@Override
	public EurekaClient get() {
		return this.client;
	}

}
