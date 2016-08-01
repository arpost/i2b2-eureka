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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.eurekaclinical.eureka.client.EurekaClient;
import org.eurekaclinical.i2b2.client.I2b2PdoRetriever;
import org.eurekaclinical.i2b2.client.I2b2PdoRetrieverImpl;
import org.eurekaclinical.i2b2.client.I2b2UserAuthenticator;
import org.eurekaclinical.i2b2.client.I2b2UserAuthenticatorImpl;
import org.eurekaclinical.i2b2.provider.ServicesClientProvider;
import org.eurekaclinical.i2b2.client.props.I2b2Properties;
import org.eurekaclinical.i2b2.dao.JpaRoleDao;
import org.eurekaclinical.i2b2.dao.JpaUserDao;
import org.eurekaclinical.i2b2.entity.RoleEntity;
import org.eurekaclinical.i2b2.entity.UserEntity;
import org.eurekaclinical.i2b2.props.I2b2EurekaServicesProperties;
import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.standardapis.dao.UserDao;

/**
 * Configuration for Guice interface bindings.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class GuiceConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(I2b2UserAuthenticator.class).to(I2b2UserAuthenticatorImpl.class);
		bind(I2b2PdoRetriever.class).to(I2b2PdoRetrieverImpl.class);
		bind(EurekaClient.class).toProvider(ServicesClientProvider.class);
		bind(I2b2Properties.class).to(I2b2EurekaServicesProperties.class);
		bind(new TypeLiteral<UserDao<UserEntity>>() {}).to(JpaUserDao.class);
        bind(new TypeLiteral<UserDao<? extends org.eurekaclinical.standardapis.entity.UserEntity<? extends org.eurekaclinical.standardapis.entity.RoleEntity>>>() {}).to(JpaUserDao.class);
        bind(new TypeLiteral<RoleDao<RoleEntity>>() {}).to(JpaRoleDao.class);
	}
}
