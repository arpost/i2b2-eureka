package org.eurekaclinical.i2b2.config;

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

import java.util.Map;
import org.eurekaclinical.common.config.AbstractAuthorizingJerseyServletModuleWithPersist;
import org.eurekaclinical.i2b2.props.I2b2EurekaServicesProperties;


/**
 * Jersey servlet configuration.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class ServletConfigModule extends AbstractAuthorizingJerseyServletModuleWithPersist {
	private static final String PACKAGE_NAMES = "org.eurekaclinical.i2b2.resource";
	
	public ServletConfigModule(I2b2EurekaServicesProperties inProperties) {
		super(inProperties, PACKAGE_NAMES);
	}
	
	@Override
	public Map<String, String> getCasValidationFilterInitParams() {
		Map<String, String> params = super.getCasValidationFilterInitParams();
		params.put("proxyCallbackUrl", getCasProxyCallbackUrl());
        params.put("proxyReceptorUrl", getCasProxyCallbackPath());
		return params;
	}
	
}
