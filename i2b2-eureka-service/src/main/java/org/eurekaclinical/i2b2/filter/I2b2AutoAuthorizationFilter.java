package org.eurekaclinical.i2b2.filter;

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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eurekaclinical.i2b2.entity.RoleEntity;
import org.eurekaclinical.i2b2.entity.UserEntity;
import org.eurekaclinical.i2b2.entity.UserTemplateEntity;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.eurekaclinical.common.filter.AbstractAutoAuthorizationFilter;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class I2b2AutoAuthorizationFilter extends AbstractAutoAuthorizationFilter<RoleEntity, UserEntity, UserTemplateEntity> {

	@Inject
	public I2b2AutoAuthorizationFilter(
			UserTemplateDao<UserTemplateEntity> inUserTemplateDao,
			UserDao<UserEntity> inUserDao) {
		super(inUserTemplateDao, inUserDao);
	}

	@Override
	protected UserEntity toUserEntity(UserTemplateEntity userTemplate, String username) {
		UserEntity user = new UserEntity();
		user.setUsername(username);
		user.setGroups(userTemplate.getGroups());
		user.setRoles(userTemplate.getRoles());
		return user;
	}

}
