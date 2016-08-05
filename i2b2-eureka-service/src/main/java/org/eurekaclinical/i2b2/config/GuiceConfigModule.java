package org.eurekaclinical.i2b2.config;

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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.eurekaclinical.eureka.client.EurekaClient;
import org.eurekaclinical.i2b2.provider.EurekaClientProvider;
import org.eurekaclinical.i2b2.dao.I2b2DomainDao;
import org.eurekaclinical.i2b2.dao.I2b2ProjectDao;
import org.eurekaclinical.i2b2.dao.I2b2RoleDao;
import org.eurekaclinical.i2b2.dao.JpaGroupDao;
import org.eurekaclinical.i2b2.dao.JpaI2b2DomainDao;
import org.eurekaclinical.i2b2.dao.JpaI2b2ProjectDao;
import org.eurekaclinical.i2b2.dao.JpaRoleDao;
import org.eurekaclinical.i2b2.dao.JpaI2b2RoleDao;
import org.eurekaclinical.i2b2.dao.JpaUserDao;
import org.eurekaclinical.i2b2.dao.JpaUserTemplateDao;
import org.eurekaclinical.i2b2.entity.GroupEntity;
import org.eurekaclinical.i2b2.entity.I2b2DomainEntity;
import org.eurekaclinical.i2b2.entity.I2b2ProjectEntity;
import org.eurekaclinical.i2b2.entity.I2b2RoleEntity;
import org.eurekaclinical.i2b2.entity.RoleEntity;
import org.eurekaclinical.i2b2.entity.UserEntity;
import org.eurekaclinical.i2b2.entity.UserTemplateEntity;
import org.eurekaclinical.standardapis.dao.GroupDao;
import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.eurekaclinical.i2b2.client.I2b2ClientFactory;
import org.eurekaclinical.i2b2.client.I2b2ClientFactoryImpl;
import org.eurekaclinical.i2b2.client.I2b2UserSetterFactory;
import org.eurekaclinical.i2b2.client.I2b2UserSetterFactoryImpl;
import org.eurekaclinical.i2b2.provider.EurekaClinicalUserAgreementClientProvider;
import org.eurekaclinical.useragreement.client.EurekaClinicalUserAgreementClient;

/**
 * Configuration for Guice interface bindings.
 *
 * @author Michel Mansour
 * @since 1.0
 */
public class GuiceConfigModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(I2b2ClientFactory.class).to(I2b2ClientFactoryImpl.class);
		bind(I2b2UserSetterFactory.class).to(I2b2UserSetterFactoryImpl.class);
		bind(EurekaClient.class).toProvider(EurekaClientProvider.class);
		bind(EurekaClinicalUserAgreementClient.class).toProvider(EurekaClinicalUserAgreementClientProvider.class);
		bind(new TypeLiteral<UserDao<UserEntity>>() {}).to(JpaUserDao.class);
		bind(new TypeLiteral<UserTemplateDao<UserTemplateEntity>>() {}).to(JpaUserTemplateDao.class);
        bind(new TypeLiteral<UserDao<? extends org.eurekaclinical.standardapis.entity.UserEntity<? extends org.eurekaclinical.standardapis.entity.RoleEntity>>>() {}).to(JpaUserDao.class);
        bind(new TypeLiteral<RoleDao<RoleEntity>>() {}).to(JpaRoleDao.class);
		bind(new TypeLiteral<GroupDao<GroupEntity>>() {}).to(JpaGroupDao.class);
		bind(new TypeLiteral<I2b2ProjectDao<I2b2ProjectEntity>>() {}).to(JpaI2b2ProjectDao.class);
		bind(new TypeLiteral<I2b2RoleDao<I2b2RoleEntity>>() {}).to(JpaI2b2RoleDao.class);
		bind(I2b2RoleDao.class).to(JpaI2b2RoleDao.class);
		bind(new TypeLiteral<I2b2DomainDao<I2b2DomainEntity>>() {}).to(JpaI2b2DomainDao.class);
	}
}
